package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemCartBinding
import com.example.myapplication.model.CartItem

/**
 * CartAdapter binds the cart item list to the Cart screen RecyclerView.
 *
 * Three actions are exposed via lambdas:
 * - [onIncrease]  → + button tapped
 * - [onDecrease]  → - button tapped (removes item if qty becomes 0)
 * - [onRemove]    → trash icon tapped (always removes)
 *
 * The ViewModel / CartManager handles the actual state mutation.
 * After mutation, the Activity refreshes this adapter via updateList().
 */
class CartAdapter(
    private var cartItems: List<CartItem>,
    private val onIncrease: (Int) -> Unit,
    private val onDecrease: (Int) -> Unit,
    private val onRemove: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        
        // Reset view state (prevent stuck swiped state after undo)
        holder.itemView.alpha = 1f
        holder.itemView.translationX = 0f

        with(holder.binding) {
            // Product image using Glide
            com.bumptech.glide.Glide.with(holder.itemView.context)
                .load(item.product.imageUrl)
                .placeholder(com.example.myapplication.R.drawable.ic_grocery_logo)
                .into(imgCartProduct)
            
            tvCartProductName.text  = item.product.name
            tvCartUnitPrice.text    = "₹${item.product.price.toInt()} each"

            // Current quantity
            tvQuantity.text = item.quantity.toString()

            // Line total (price × quantity)
            tvCartItemTotal.text = "₹${item.totalPrice().toInt()}"

            // Button callbacks — pass the product ID to the ViewModel
            btnIncrease.setOnClickListener { onIncrease(item.product.id) }
            btnDecrease.setOnClickListener { onDecrease(item.product.id) }
            btnRemove.setOnClickListener   { onRemove(item.product.id) }
        }
    }

    override fun getItemCount(): Int = cartItems.size

    /** Replace the displayed list (called after any cart mutation). */
    fun updateList(newItems: List<CartItem>) {
        cartItems = newItems
        notifyDataSetChanged()
    }
}
