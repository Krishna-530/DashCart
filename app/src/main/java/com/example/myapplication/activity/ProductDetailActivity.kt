package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityProductDetailBinding
import com.example.myapplication.utils.CartManager
import com.example.myapplication.utils.DummyData

/**
 * ProductDetailActivity — full-page product view with description,
 * quantity stepper, and Add-to-Cart.
 */
class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        val product   = DummyData.getById(productId)

        if (product == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // ── Toolbar ──────────────────────────────────────────────────────────
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = product.name
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // ── Load image ────────────────────────────────────────────────────────
        Glide.with(this)
            .load(product.imageUrl)
            .placeholder(R.drawable.ic_grocery_logo)
            .into(binding.imgProductDetail)

        // ── Fill info ─────────────────────────────────────────────────────────
        binding.tvDetailCompany.text     = product.company
        binding.tvDetailName.text        = product.name
        binding.tvDetailCategory.text    = "Category: ${product.category}"
        binding.tvDetailPrice.text       = "₹${product.price.toInt()}"
        binding.tvDetailDescription.text = product.description.ifEmpty {
            "A quality product by ${product.company}. Fresh and delivered to your door."
        }

        // ── Out of stock ──────────────────────────────────────────────────────
        if (product.isOutOfStock) {
            binding.tvOutOfStockBadge.visibility = View.VISIBLE
            binding.btnDetailAddToCart.isEnabled = false
            binding.btnDetailAddToCart.text      = "Out of Stock"
            binding.btnDetailAddToCart.alpha     = 0.5f
            binding.layoutQtyStepper.visibility  = View.GONE
        }

        // ── Quantity stepper ──────────────────────────────────────────────────
        updateQtyDisplay()

        binding.btnDetailIncrease.setOnClickListener {
            quantity++
            updateQtyDisplay()
        }
        binding.btnDetailDecrease.setOnClickListener {
            if (quantity > 1) { quantity--; updateQtyDisplay() }
        }

        // ── Add to Cart ───────────────────────────────────────────────────────
        binding.btnDetailAddToCart.setOnClickListener {
            repeat(quantity) { CartManager.addToCart(product) }
            Toast.makeText(
                this,
                "✅ ${product.name} ×$quantity added to cart!",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun updateQtyDisplay() {
        binding.tvDetailQty.text = quantity.toString()
        binding.btnDetailAddToCart.text = "Add $quantity to Cart · ₹${(binding.tvDetailPrice.text.toString().replace("₹","").toIntOrNull() ?: 0) * quantity}"
    }
}
