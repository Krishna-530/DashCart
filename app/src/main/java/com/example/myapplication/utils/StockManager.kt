package com.example.myapplication.utils

import com.example.myapplication.model.Product

/**
 * StockManager — Singleton that manages live (in-memory) stock counts.
 *
 * Initialised once from DummyData product stockCounts.
 * CartManager calls [decrementStock] / [restoreStock] whenever the cart changes.
 * UI layers observe changes via [getStock] / [isLowStock] / [isOutOfStock].
 *
 * LOW_STOCK threshold: ≤ 3 units remaining.
 */
object StockManager {

    private const val LOW_STOCK_THRESHOLD = 3

    // productId → current available stock
    private val stockMap = mutableMapOf<Int, Int>()

    /** Seed stock from the product list (call once at app start). */
    fun init(products: List<Product>) {
        products.forEach { product ->
            // If already initialised (e.g. after screen rotation), keep existing value
            if (!stockMap.containsKey(product.id)) {
                stockMap[product.id] = if (product.isOutOfStock) 0 else product.stockCount
            }
        }
    }

    /** Get current stock count for a product. Returns 0 if unknown. */
    fun getStock(productId: Int): Int = stockMap[productId] ?: 0

    /** Returns true if stock ≤ LOW_STOCK_THRESHOLD and > 0. */
    fun isLowStock(productId: Int): Boolean {
        val s = getStock(productId)
        return s in 1..LOW_STOCK_THRESHOLD
    }

    /** Returns true if stock is exactly 0. */
    fun isOutOfStock(productId: Int): Boolean = getStock(productId) == 0

    /**
     * Decrement stock by [qty] when product is added to cart.
     * Ensures stock never goes below 0.
     * Returns false if insufficient stock (caller should prevent add).
     */
    fun decrementStock(productId: Int, qty: Int = 1): Boolean {
        val current = stockMap[productId] ?: return false
        if (current < qty) return false
        stockMap[productId] = current - qty
        return true
    }

    /**
     * Restore stock by [qty] when product is removed from cart or quantity is reduced.
     */
    fun restoreStock(productId: Int, qty: Int = 1) {
        val current = stockMap[productId] ?: return
        stockMap[productId] = current + qty
    }

    /** Check if at least [qty] units can be added (for + button guard). */
    fun canAdd(productId: Int, qty: Int = 1): Boolean = getStock(productId) >= qty
}
