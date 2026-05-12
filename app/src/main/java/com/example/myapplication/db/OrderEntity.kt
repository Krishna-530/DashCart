package com.example.myapplication.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * OrderEntity — Represents a placed order stored in Room DB.
 *
 * Each order stores a summary snapshot of the cart at checkout time.
 * Individual ordered items are stored as a comma-separated string for simplicity.
 */
@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val orderId: Long = 0,

    /** Human-readable order reference like "GR-84291" */
    val orderRef: String,

    /** Comma-separated "Name x Qty" entries, e.g. "Amul Butter x2, Milk x1" */
    val itemsSummary: String,

    /** Grand total paid (includes taxes, fees) */
    val totalAmount: Double,

    /** Unix timestamp (milliseconds) of when order was placed */
    val timestamp: Long = System.currentTimeMillis(),

    /** Static fake delivery status */
    val deliveryStatus: String = "Delivered",

    /** Payment method used */
    val paymentMethod: String = "Cash on Delivery"
)
