package com.example.myapplication.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * OrderDao — Data Access Object for the orders table.
 *
 * All queries run on background threads via Room + LiveData / suspend functions.
 */
@Dao
interface OrderDao {

    /** Insert a new order. Returns the auto-generated orderId. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    /** Observe all orders, latest first (descending timestamp). */
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrdersLive(): LiveData<List<OrderEntity>>

    /** One-shot fetch of all orders (for non-reactive use). */
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    suspend fun getAllOrders(): List<OrderEntity>

    /** Delete all orders (for testing / reset). */
    @Query("DELETE FROM orders")
    suspend fun clearAll()
}
