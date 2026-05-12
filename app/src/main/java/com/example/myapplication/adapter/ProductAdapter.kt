package com.example.myapplication.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemProductBinding
import com.example.myapplication.model.Product
import kotlin.math.roundToInt

/**
 * ProductAdapter — 2-column grid for the "All Products" section and
 * category-filtered views. Uses [item_product.xml] (LinearLayout structure,
 * consistent with the horizontal card style).
 */
class ProductAdapter(
    private var products: List<Product>,
    private val onCartUpdated: (Product) -> Unit,
    private val onProductClick: (Product) -> Unit = {}
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        with(holder.binding) {

            // Product image via Glide
            Glide.with(holder.itemView.context)
                .load(product.imageUrl)
                .placeholder(R.drawable.ic_grocery_logo)
                .error(R.drawable.ic_grocery_logo)
                .centerInside()
                .into(imgProduct)

            tvProductName.text = product.name
            tvWeight.text = product.weight.ifEmpty { "1 pack" }
            tvProductPrice.text = "₹${product.price.roundToInt()}"

            // Strikethrough original price
            if (product.originalPrice != null) {
                tvStrikethroughPrice.visibility = View.VISIBLE
                tvStrikethroughPrice.text = "₹${product.originalPrice.roundToInt()}"
                tvStrikethroughPrice.paintFlags =
                    tvStrikethroughPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvStrikethroughPrice.visibility = View.GONE
            }

            // Offer badge
            offerLayout.visibility =
                if (product.isHighestDiscount) View.VISIBLE else View.GONE

            // ── Feature 3: Stock Logic ────────────────────────────────────────
            val currentStock = com.example.myapplication.utils.StockManager.getStock(product.id)
            val isOutOfStock = currentStock == 0

            // Show "Only X left!" badge if stock in [1..3]
            if (currentStock in 1..3) {
                tvStockBadge.visibility = View.VISIBLE
                tvStockBadge.text = "Only $currentStock left!"
            } else {
                tvStockBadge.visibility = View.GONE
            }

            // Show out-of-stock overlay
            outOfStockOverlay.visibility = if (isOutOfStock) View.VISIBLE else View.GONE
            
            // Sync with CartManager
            val currentQty = com.example.myapplication.utils.CartManager.getProductQuantity(product.id)
            
            if (currentQty > 0 && !isOutOfStock) {
                btnAdd.visibility = View.GONE
                llQuantitySelector.visibility = View.VISIBLE
                tvQuantity.text = currentQty.toString()
            } else {
                btnAdd.visibility = if (isOutOfStock) View.GONE else View.VISIBLE
                llQuantitySelector.visibility = View.GONE
            }

            // Click: Add First Unit
            btnAdd.setOnClickListener {
                if (com.example.myapplication.utils.CartManager.addToCart(product)) {
                    onCartUpdated(product)
                    notifyItemChanged(holder.adapterPosition)
                }
            }

            // Click: Increase
            btnPlus.setOnClickListener {
                if (com.example.myapplication.utils.CartManager.increaseQuantity(product.id)) {
                    onCartUpdated(product)
                    notifyItemChanged(holder.adapterPosition)
                } else {
                    android.widget.Toast.makeText(holder.itemView.context, "No more stock!", android.widget.Toast.LENGTH_SHORT).show()
                }
            }

            // Click: Decrease
            btnMinus.setOnClickListener {
                com.example.myapplication.utils.CartManager.decreaseQuantity(product.id)
                onCartUpdated(product)
                notifyItemChanged(holder.adapterPosition)
            }

            cardProduct.setOnClickListener { onProductClick(product) }
        }
    }

    fun updateList(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
