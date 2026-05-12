package com.example.myapplication.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemOfferCardBinding
import com.example.myapplication.model.Product
import com.example.myapplication.ui.ProductBottomSheetFragment

/**
 * Adapter for the horizontal "Today's Offers" strip showing products with discounts.
 */
class OfferCardAdapter(
    private var products: List<Product>,
    private val onCartUpdated: (Product) -> Unit,
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<OfferCardAdapter.OfferViewHolder>() {

    inner class OfferViewHolder(
        val binding: ItemOfferCardBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val binding = ItemOfferCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val product = products[position]
        holder.binding.apply {
            tvOfferProductName.text = product.name
            tvOfferPrice.text = "₹${product.price.toInt()}"

            // Show original price with strikethrough if available
            if (product.originalPrice != null && product.originalPrice > product.price) {
                tvOfferOriginalPrice.text = "₹${product.originalPrice.toInt()}"
                tvOfferOriginalPrice.paintFlags =
                    tvOfferOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                // Calculate and show discount percentage
                val discount = ((product.originalPrice - product.price) / product.originalPrice * 100).toInt()
                tvDiscountPercent.text = "${discount}% OFF"
            } else {
                tvOfferOriginalPrice.text = ""
                tvDiscountPercent.text = "DEAL"
            }

            // Load product image
            Glide.with(ivOfferImage.context)
                .load(product.imageUrl)
                .centerCrop()
                .into(ivOfferImage)

            // Quantity Controls
            val currentQty = com.example.myapplication.utils.CartManager.getProductQuantity(product.id)
            if (currentQty > 0) {
                btnAdd.visibility = android.view.View.GONE
                llQuantitySelector.visibility = android.view.View.VISIBLE
                tvQuantity.text = currentQty.toString()

                btnPlus.setOnClickListener {
                    com.example.myapplication.utils.CartManager.increaseQuantity(product.id)
                    onCartUpdated(product)
                }

                btnMinus.setOnClickListener {
                    com.example.myapplication.utils.CartManager.decreaseQuantity(product.id)
                    onCartUpdated(product)
                }
            } else {
                btnAdd.visibility = android.view.View.VISIBLE
                llQuantitySelector.visibility = android.view.View.GONE
                btnAdd.setOnClickListener {
                    // Animate the expansion
                    androidx.transition.TransitionManager.beginDelayedTransition(holder.binding.root as android.view.ViewGroup)
                    
                    // Add to cart
                    com.example.myapplication.utils.CartManager.addToCart(product)
                    
                    onCartUpdated(product)
                }
            }

            root.setOnClickListener { onProductClick(product) }
        }
    }

    override fun getItemCount() = products.size

    fun updateList(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
