package com.example.myapplication.model

/**
 * Represents an item inside the cart.
 * Wraps a Product and tracks how many units the user added.
 *
 * @param product  The product being purchased
 * @param quantity How many units are in the cart (starts at 1)
 */
data class CartItem(
    val product: Product,
    var quantity: Int = 1
) {
    /** Computed total price for this cart line */
    fun totalPrice(): Double = product.price * quantity
}
