package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.db.OrderEntity
import android.content.Context

/**
 * OrderRepository — mediates between ViewModel and Room DAO.
 *
 * All suspend functions are called from ViewModel coroutines (viewModelScope).
 * LiveData is returned directly so Room handles background threading.
 */
class OrderRepository(context: Context) {

    private val dao = AppDatabase.getInstance(context).orderDao()

    /** LiveData stream of all orders — updates automatically when DB changes. */
    val allOrders: LiveData<List<OrderEntity>> = dao.getAllOrdersLive()

    /** Insert a new order and return the auto-generated ID. */
    suspend fun insertOrder(order: OrderEntity): Long = dao.insertOrder(order)

    /** One-shot fetch (non-reactive). */
    suspend fun getAll(): List<OrderEntity> = dao.getAllOrders()
}
