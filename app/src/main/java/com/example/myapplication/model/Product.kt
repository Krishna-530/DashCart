package com.example.myapplication.model

/**
 * Represents a grocery product shown on the Home screen.
 *
 * @param id           Unique identifier for the product
 * @param name         Display name (e.g., "Fresh Milk")
 * @param price        Price in rupees (e.g., 60.0)
 * @param imageUrl     Remote image URL for the product
 * @param category     Category label (e.g., "Dairy", "Fruits")
 * @param stockCount   Available units in stock. 0 = out of stock.
 */
data class Product(
    val id: Int,
    val name: String,
    val company: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val isOutOfStock: Boolean = false,
    val description: String = "",
    val weight: String = "",
    val originalPrice: Double? = null,
    val isFrequentlyPurchased: Boolean = false,
    val isNewlyAdded: Boolean = false,
    val isHighestDiscount: Boolean = false,
    /** Live stock count — simulated. Default 10 = comfortably in stock. */
    val stockCount: Int = 10,
    val tags: List<String> = emptyList()
)
