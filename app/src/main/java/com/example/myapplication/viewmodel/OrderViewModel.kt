package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.db.OrderEntity
import com.example.myapplication.repository.OrderRepository
import kotlinx.coroutines.launch

/**
 * OrderViewModel — provides order history data to OrderHistoryActivity.
 *
 * Extends AndroidViewModel to safely hold an Application context for Room.
 */
class OrderViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = OrderRepository(application)

    /** LiveData list of all orders — UI observes this. */
    val allOrders: LiveData<List<OrderEntity>> = repository.allOrders

    /**
     * Insert a new order record.
     * Called from CheckoutActivity after successful order placement.
     */
    fun insertOrder(order: OrderEntity) {
        viewModelScope.launch {
            repository.insertOrder(order)
        }
    }
}
