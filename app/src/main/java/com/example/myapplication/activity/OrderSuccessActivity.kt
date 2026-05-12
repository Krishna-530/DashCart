package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityOrderSuccessBinding

/**
 * OrderSuccessActivity — Shown after a successful order.
 *
 * Receives order details from CheckoutActivity via Intent extras and
 * displays a confirmation screen.
 *
 * "Continue Shopping" restarts the Home screen from scratch.
 * 
 * Note: We clear the activity task so the user can't "back" into the checkout again.
 */
class OrderSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayOrderDetails()
        setupContinueButton()
    }

    private fun displayOrderDetails() {
        // Retrieve order details passed from CheckoutActivity
        val address  = intent.getStringExtra("ADDRESS") ?: "N/A"
        val payment  = intent.getStringExtra("PAYMENT") ?: "N/A"
        val orderId  = intent.getStringExtra("ORDER_ID") ?: "GR-${java.util.Random().nextInt(90000) + 10000}"
        val total    = intent.getIntExtra("TOTAL", 0)
        val itemCount = intent.getIntExtra("ITEMS", 0)

        // Build a readable order summary string
        binding.tvOrderDetails.text =
            "🔢 Order ID: $orderId\n" +
            "📦 $itemCount item(s) ordered\n" +
            "💰 Total: ₹$total\n" +
            "💳 Payment: $payment\n" +
            "📍 Delivery to: $address"
    }

    private fun setupContinueButton() {
        binding.btnContinueShopping.setOnClickListener {
            // Clear the back stack and restart HomeActivity fresh
            val intent = Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}
