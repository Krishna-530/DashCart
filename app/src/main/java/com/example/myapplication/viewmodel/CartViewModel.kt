package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.CartItem
import com.example.myapplication.utils.CartManager

/**
 * CartViewModel provides LiveData-wrapped cart state to CartActivity.
 *
 * It delegates all actual logic to the CartManager singleton,
 * but exposes the results as LiveData so the UI auto-updates.
 */
class CartViewModel : ViewModel() {

    // LiveData list of cart items — CartActivity observes this
    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    // LiveData total bill amount
    private val _subtotal = MutableLiveData<Double>()
    val subtotal: LiveData<Double> = _subtotal
    
    private val _tax = MutableLiveData<Double>()
    val tax: LiveData<Double> = _tax
    
    private val _deliveryFee = MutableLiveData<Double>()
    val deliveryFee: LiveData<Double> = _deliveryFee
    
    private val _platformFee = MutableLiveData<Double>()
    val platformFee: LiveData<Double> = _platformFee

    private val _grandTotal = MutableLiveData<Double>()
    val grandTotal: LiveData<Double> = _grandTotal

    private val _discount = MutableLiveData<Double>()
    val discount: LiveData<Double> = _discount

    init {
        // Load initial state when ViewModel is first created
        refreshCart()
    }

    /** Re-read cart state from CartManager and push to LiveData. */
    fun refreshCart() {
        // Create a new list copy so LiveData detects the change
        _cartItems.value = CartManager.cartItems.toList()
        _subtotal.value = CartManager.getSubtotal()
        _tax.value = CartManager.getTax()
        _deliveryFee.value = CartManager.getDeliveryFee()
        _platformFee.value = CartManager.getPlatformFee()
        _discount.value = CartManager.getDiscount()
        _grandTotal.value = CartManager.getGrandTotal()
    }

    fun applyCoupon(code: String): Boolean {
        val amount = when (code.uppercase()) {
            "SAVE50" -> 50.0
            "FRESH100" -> 100.0
            "WELCOME" -> 20.0
            else -> 0.0
        }
        if (amount > 0) {
            CartManager.applyDiscount(amount)
            refreshCart()
            return true
        }
        return false
    }

    fun increaseQuantity(productId: Int) {
        CartManager.increaseQuantity(productId)
        refreshCart()
    }

    fun decreaseQuantity(productId: Int) {
        CartManager.decreaseQuantity(productId)
        refreshCart()
    }

    /**
     * Remove item and return its snapshot for undo (Feature 1: swipe-to-delete).
     * Returns a copy of the CartItem with its quantity at time of deletion.
     */
    fun removeItem(productId: Int): CartItem? {
        // Snapshot before removal
        val snapshot = CartManager.cartItems.find { it.product.id == productId }
            ?.let { CartItem(it.product, it.quantity) }
        CartManager.removeFromCart(productId)
        refreshCart()
        return snapshot
    }

    /**
     * Restore a previously swiped-away item (Feature 1: undo).
     * Returns true if the item was successfully restored.
     */
    fun restoreItem(cartItem: CartItem): Boolean {
        val success = CartManager.restoreCartItem(cartItem)
        refreshCart()
        return success
    }
}

