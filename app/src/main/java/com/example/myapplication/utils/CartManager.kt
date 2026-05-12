package com.example.myapplication.utils

import com.example.myapplication.model.CartItem
import com.example.myapplication.model.Product

/**
 * CartManager is a simple Singleton object that holds the cart state in memory.
 *
 * Why a Singleton?
 * - No Firebase / database needed (dummy data only)
 * - All activities/viewmodels share the same cart instance
 * - Easy to understand for beginners
 *
 * Also hooks into StockManager so that stock counts stay in sync with cart ops.
 */
// Simple singleton to keep track of the cart. 
// Using a singleton for now since we don't need a persistent cart DB for the assignment, 
// but might move to Room later if the scope increases.
object CartManager {

    // Internal mutable list — only this file can modify it directly
    private val _cartItems = mutableListOf<CartItem>()

    // Read-only view exposed to the rest of the app
    val cartItems: List<CartItem> get() = _cartItems

    /** Get current quantity of a specific product in the cart. */
    fun getProductQuantity(productId: Int): Int {
        return _cartItems.find { it.product.id == productId }?.quantity ?: 0
    }

    /**
     * Add a product to the cart.
     * Checks stock before adding — returns false if out of stock.
     * If already in cart, increments quantity (up to available stock).
     */
    fun addToCart(product: Product): Boolean {
        if (!StockManager.canAdd(product.id)) return false
        val existing = _cartItems.find { it.product.id == product.id }
        return if (existing != null) {
            existing.quantity++
            StockManager.decrementStock(product.id)
            true
        } else {
            _cartItems.add(CartItem(product, 1))
            StockManager.decrementStock(product.id)
            true
        }
    }

    /** Increase quantity of an existing cart item by 1 (stock-guarded). */
    fun increaseQuantity(productId: Int): Boolean {
        if (!StockManager.canAdd(productId)) return false
        _cartItems.find { it.product.id == productId }?.let {
            it.quantity++
            StockManager.decrementStock(productId)
            return true
        }
        return false
    }

    /** Decrease quantity; removes item entirely when quantity reaches 0. */
    fun decreaseQuantity(productId: Int) {
        val item = _cartItems.find { it.product.id == productId } ?: return
        StockManager.restoreStock(productId)
        if (item.quantity > 1) {
            item.quantity--
        } else {
            _cartItems.remove(item)
        }
    }

    /** Completely remove an item from the cart regardless of quantity. */
    fun removeFromCart(productId: Int) {
        val item = _cartItems.find { it.product.id == productId } ?: return
        // Restore all units of this item back to stock
        StockManager.restoreStock(productId, item.quantity)
        _cartItems.remove(item)
    }

    /**
     * Restore a previously removed CartItem back into the cart (used by swipe-undo).
     * Decrements stock by the full quantity of the restored item.
     * Returns false if stock is now insufficient.
     */
    fun restoreCartItem(cartItem: CartItem): Boolean {
        if (StockManager.getStock(cartItem.product.id) < cartItem.quantity) return false
        val existing = _cartItems.find { it.product.id == cartItem.product.id }
        return if (existing != null) {
            existing.quantity += cartItem.quantity
            StockManager.decrementStock(cartItem.product.id, cartItem.quantity)
            true
        } else {
            _cartItems.add(CartItem(cartItem.product, cartItem.quantity))
            StockManager.decrementStock(cartItem.product.id, cartItem.quantity)
            true
        }
    }

    /** Calculate the subtotal of all items in the cart. */
    fun getSubtotal(): Double = _cartItems.sumOf { it.totalPrice() }

    /** Platform handling fee (fixed or calculated). */
    fun getPlatformFee(): Double = if (isEmpty()) 0.0 else 5.0

    /** Delivery fee. Free if subtotal >= 500. */
    fun getDeliveryFee(): Double = if (isEmpty() || getSubtotal() >= 500) 0.0 else 30.0

    /** Tax calculation (e.g., 5% GST on subtotal). */
    fun getTax(): Double = if (isEmpty()) 0.0 else getSubtotal() * 0.05

    // ── Coupon / Discount Support (Feature: Predefined Coupons) ─────────────
    private var appliedDiscount: Double = 0.0

    fun applyDiscount(amount: Double) {
        appliedDiscount = amount
    }

    fun getDiscount(): Double = appliedDiscount

    /** The final total to be paid. */
    fun getGrandTotal(): Double {
        // Adding everything up. Platform fee and tax only apply if cart isn't empty.
        val total = getSubtotal() + getPlatformFee() + getDeliveryFee() + getTax() - appliedDiscount
        return if (total < 0) 0.0 else total
    }

    /** Total number of individual units in the cart (for badge display). */
    fun getItemCount(): Int = _cartItems.sumOf { it.quantity }

    /** 
     * Remove everything from the cart.
     * @param restore If true, the stock counts for these items are restored to the inventory.
     *                Set to false when clearing after a successful order placement.
     */
    fun clearCart(restore: Boolean = true) {
        if (restore) {
            _cartItems.forEach { StockManager.restoreStock(it.product.id, it.quantity) }
        }
        _cartItems.clear()
        appliedDiscount = 0.0
    }

    /** Check if the cart is empty. */
    fun isEmpty(): Boolean = _cartItems.isEmpty()
}

