package com.example.myapplication.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityCheckoutBinding
import com.example.myapplication.utils.CartManager
import com.example.myapplication.utils.SharedPrefsHelper
import com.google.android.material.textfield.TextInputEditText
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

/**
 * CheckoutActivity — address, payment selection, bill breakdown, and order placement.
 *
 * COD  → confirm dialog → clears cart → OrderTrackingActivity
 * UPI  → fake UPI dialog with 3-second payment animation → OrderTrackingActivity
 */
class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        showOrderSummary()
        preFillAddress()
        setupPlaceOrderButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Checkout"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun preFillAddress() {
        val saved = SharedPrefsHelper.getSavedAddress(this)
        if (saved.isNotEmpty()) binding.etAddress.setText(saved)
    }

    private fun showOrderSummary() {
        binding.tvCheckoutSubtotal.text    = "₹${CartManager.getSubtotal().toInt()}"
        binding.tvCheckoutPlatformFee.text = "₹${CartManager.getPlatformFee().toInt()}"
        binding.tvCheckoutDeliveryFee.text =
            if (CartManager.getDeliveryFee() == 0.0) "FREE" else "₹${CartManager.getDeliveryFee().toInt()}"
        binding.tvCheckoutTax.text         = "₹${CartManager.getTax().toInt()}"
        binding.tvCheckoutGrandTotal.text  = "₹${CartManager.getGrandTotal().toInt()}"
        binding.btnPlaceOrder.text         = "Place Order · ₹${CartManager.getGrandTotal().toInt()}"
    }

    private fun setupPlaceOrderButton() {
        binding.rbCod.isChecked = true

        // Toggle card details visibility
        binding.rgPayment.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == binding.rbOnline.id) {
                binding.layoutCardDetails.visibility = android.view.View.VISIBLE
            } else {
                binding.layoutCardDetails.visibility = android.view.View.GONE
            }
        }

        binding.btnPlaceOrder.setOnClickListener {
            val address = binding.etAddress.text.toString().trim()
            if (address.length < 10) {
                binding.etAddress.error = "Please enter a complete delivery address"
                return@setOnClickListener
            }

            if (binding.rbOnline.isChecked) {
                val cardNo = binding.etCardNumber.text.toString().trim()
                val expiry = binding.etExpiry.text.toString().trim()
                val cvv    = binding.etCVV.text.toString().trim()

                if (cardNo.length < 16) {
                    binding.etCardNumber.error = "Invalid Card Number"
                    return@setOnClickListener
                }
                if (expiry.length < 5) {
                    binding.etExpiry.error = "Invalid Expiry"
                    return@setOnClickListener
                }
                if (cvv.length < 3) {
                    binding.etCVV.error = "Invalid CVV"
                    return@setOnClickListener
                }

                showPaymentProcessing(address, "Card Payment")
            } else {
                confirmAndNavigate(address, "Cash on Delivery")
            }
        }
    }

    private fun showPaymentProcessing(address: String, paymentMethod: String) {
        val processingDialog = AlertDialog.Builder(this)
            .setTitle("⏳ Processing Payment…")
            .setMessage("Securing transaction\n\nAmount: ₹${CartManager.getGrandTotal().toInt()}")
            .setCancelable(false)
            .create()
        processingDialog.show()

        // After 2.5 seconds simulate success
        Handler(Looper.getMainLooper()).postDelayed({
            processingDialog.dismiss()
            AlertDialog.Builder(this)
                .setTitle("✅ Payment Successful!")
                .setMessage("₹${CartManager.getGrandTotal().toInt()} paid successfully.\n\nYour order is confirmed!")
                .setPositiveButton("Track Order") { _, _ ->
                    confirmAndNavigate(address, paymentMethod)
                }
                .setCancelable(false)
                .show()
        }, 2500)
    }


    private fun confirmAndNavigate(address: String, paymentMethod: String) {
        // ── Feature 2: Save Order to History ───────────────────────────────
        val orderItemsSummary = CartManager.cartItems.joinToString("\n") { 
            "${it.product.name} x ${it.quantity}" 
        }
        val orderTotal = CartManager.getGrandTotal()
        val orderRef = "GR-${java.util.Random().nextInt(90000) + 10000}"
        
        val orderEntity = com.example.myapplication.db.OrderEntity(
            orderRef = orderRef,
            itemsSummary = orderItemsSummary,
            totalAmount = orderTotal,
            timestamp = System.currentTimeMillis(),
            deliveryStatus = "Confirmed",
            paymentMethod = paymentMethod
        )

        // Save active order for persistent tracking (Feature 4)
        com.example.myapplication.utils.SharedPrefsHelper.saveActiveOrder(
            this, orderRef, System.currentTimeMillis()
        )

        // Save order in background (coroutine)
        lifecycleScope.launch {
            try {
                com.example.myapplication.db.AppDatabase.getInstance(this@CheckoutActivity)
                    .orderDao()
                    .insertOrder(orderEntity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // ── Feature 5: Send Order Notification ─────────────────────────────
        lifecycleScope.launch {
            com.example.myapplication.repository.NotificationRepository(this@CheckoutActivity)
                .addNotification(
                    "🛒 Order Confirmed!",
                    "Your order $orderRef has been placed successfully and will be delivered soon.",
                    "ORDER",
                    orderRef
                )
        }

        // Clear cart and navigate
        CartManager.clearCart(restore = false)
        val intent = Intent(this, OrderTrackingActivity::class.java).apply {
            putExtra("ADDRESS",  address)
            putExtra("PAYMENT",  paymentMethod)
        }
        startActivity(intent)
        finishAffinity()
    }
}
