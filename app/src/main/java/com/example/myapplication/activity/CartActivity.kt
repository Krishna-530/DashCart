package com.example.myapplication.activity

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.CartAdapter
import com.example.myapplication.databinding.ActivityCartBinding
import com.example.myapplication.model.CartItem
import com.example.myapplication.utils.CartManager
import com.example.myapplication.adapter.HorizontalProductAdapter
import com.example.myapplication.utils.DummyData
import com.example.myapplication.viewmodel.CartViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * CartActivity — Shows all items currently in the cart.
 *
 * Features:
 *  - RecyclerView of cart items (with +/- quantity controls)
 *  - Feature 1: Swipe left/right to remove item with undo Snackbar
 *  - Dynamic total bill that updates on every change
 *  - Empty cart state (shows message + hides proceed button)
 *  - "Proceed to Checkout" navigates to CheckoutActivity
 */
class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val viewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter
    private lateinit var quickAddAdapter: HorizontalProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupSwipeToDismiss()
        setupQuickAdd()
        observeViewModel()
        setupCheckoutButton()
        setupCouponAndPayment()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "My Cart"
        // Show back arrow to go back to HomeActivity
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            cartItems = emptyList(),
            onIncrease = { productId -> viewModel.increaseQuantity(productId) },
            onDecrease = { productId -> viewModel.decreaseQuantity(productId) },
            onRemove   = { productId -> swipeRemoveItem(productId) }
        )
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = cartAdapter
        }
    }

    /**
     * Feature 1: Swipe-to-dismiss with undo Snackbar.
     *
     * Both LEFT and RIGHT swipes are supported.
     * Red background with trash icon is drawn behind the swiped item.
     * After removal, a Snackbar with UNDO action appears for 4 seconds.
     */
    private fun setupSwipeToDismiss() {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val items = viewModel.cartItems.value ?: return
                if (position < 0 || position >= items.size) return

                val productId = items[position].product.id
                swipeRemoveItem(productId)
            }

            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val paint = Paint().apply { color = Color.parseColor("#FFEF4444") }
                    val cornerRadius = 14f * resources.displayMetrics.density

                    if (dX > 0) {
                        // Swiping RIGHT → draw red background on left side
                        val rect = RectF(
                            itemView.left.toFloat(), itemView.top.toFloat(),
                            itemView.left + dX, itemView.bottom.toFloat()
                        )
                        c.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
                    } else if (dX < 0) {
                        // Swiping LEFT → draw red background on right side
                        val rect = RectF(
                            itemView.right + dX, itemView.top.toFloat(),
                            itemView.right.toFloat(), itemView.bottom.toFloat()
                        )
                        c.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
                    }

                    // Draw delete icon text
                    val textPaint = Paint().apply {
                        color = Color.WHITE
                        textSize = 13f * resources.displayMetrics.density
                        isAntiAlias = true
                        textAlign = if (dX > 0) Paint.Align.LEFT else Paint.Align.RIGHT
                    }
                    val textX = if (dX > 0) itemView.left + 24f * resources.displayMetrics.density
                                else itemView.right - 24f * resources.displayMetrics.density
                    val textY = (itemView.top + itemView.bottom) / 2f + 5f * resources.displayMetrics.density
                    c.drawText("🗑 Remove", textX, textY, textPaint)

                    // Fade the card as it's being swiped
                    val alpha = 1f - Math.abs(dX) / itemView.width.toFloat()
                    itemView.alpha = alpha
                    itemView.translationX = dX
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }
        }

        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.rvCart)
    }

    /**
     * Removes item from cart and shows undo Snackbar.
     * If undo is tapped, the item is restored with its original quantity.
     */
    private fun swipeRemoveItem(productId: Int) {
        val removedItem: CartItem? = viewModel.removeItem(productId)
        if (removedItem == null) return

        Snackbar.make(binding.root, "Item removed from cart", Snackbar.LENGTH_LONG)
            .setDuration(4000)
            .setAction("UNDO") {
                val restored = viewModel.restoreItem(removedItem)
                if (!restored) {
                    Toast.makeText(this, "Could not restore — stock unavailable", Toast.LENGTH_SHORT).show()
                }
            }
            .setActionTextColor(Color.parseColor("#2DC56D"))
            .show()
    }

    private fun setupQuickAdd() {
        // Pick some products that are not already in the cart for "Quick Add"
        val suggestedProducts = DummyData.getProducts().shuffled().take(6)
        
        quickAddAdapter = HorizontalProductAdapter(
            products = suggestedProducts,
            onCartUpdated = { 
                viewModel.refreshCart()
                // Refresh quick add list to update quantity buttons if needed
                quickAddAdapter.notifyDataSetChanged()
            },
            onProductClick = { product ->
                val intent = Intent(this, ProductDetailActivity::class.java)
                intent.putExtra("PRODUCT_ID", product.id)
                startActivity(intent)
            }
        )

        binding.rvQuickAdd.apply {
            layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = quickAddAdapter
        }
    }

    private fun observeViewModel() {
        // Observe cart items list
        viewModel.cartItems.observe(this) { items ->
            cartAdapter.updateList(items)

            // Toggle empty state visibility
            if (items.isEmpty()) {
                binding.tvEmptyCart.visibility       = View.VISIBLE
                binding.scrollView.visibility        = View.GONE
                binding.btnProceedCheckout.visibility = View.GONE
                binding.cardTotal.visibility          = View.GONE
            } else {
                binding.tvEmptyCart.visibility       = View.GONE
                binding.scrollView.visibility        = View.VISIBLE
                binding.btnProceedCheckout.visibility = View.VISIBLE
                binding.cardTotal.visibility          = View.VISIBLE
            }
        }

        // Observe billing parts
        viewModel.subtotal.observe(this) { binding.tvSubtotal.text = "₹${it.toInt()}" }
        viewModel.tax.observe(this) { binding.tvTax.text = "₹${it.toInt()}" }
        viewModel.deliveryFee.observe(this) { binding.tvDeliveryFee.text = "₹${it.toInt()}" }
        viewModel.platformFee.observe(this) { binding.tvPlatformFee.text = "₹${it.toInt()}" }
        viewModel.discount.observe(this) { 
            if (it > 0) {
                binding.layoutDiscount.visibility = View.VISIBLE
                binding.tvDiscount.text = "-₹${it.toInt()}"
            } else {
                binding.layoutDiscount.visibility = View.GONE
            }
        }
        viewModel.grandTotal.observe(this) { binding.tvGrandTotal.text = "₹${it.toInt()}" }
    }

    private fun setupCouponAndPayment() {
        binding.btnApplyCoupon.setOnClickListener {
            val code = binding.etCouponCode.text.toString().trim()
            if (code.isEmpty()) {
                Toast.makeText(this, "Please enter a coupon code", Toast.LENGTH_SHORT).show()
            } else {
                val success = viewModel.applyCoupon(code)
                if (success) {
                    Toast.makeText(this, "🎟️ Coupon Applied! Discount added.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Invalid coupon code", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupCheckoutButton() {
        binding.btnProceedCheckout.setOnClickListener {
            if (CartManager.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(Intent(this, CheckoutActivity::class.java))
        }
    }
}

