package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemOrderBinding
import com.example.myapplication.db.OrderEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * OrderHistoryAdapter — displays order history cards using ListAdapter + DiffUtil.
 */
class OrderHistoryAdapter : ListAdapter<OrderEntity, OrderHistoryAdapter.OrderViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<OrderEntity>() {
            override fun areItemsTheSame(a: OrderEntity, b: OrderEntity) = a.orderId == b.orderId
            override fun areContentsTheSame(a: OrderEntity, b: OrderEntity) = a == b
        }
    }

    inner class OrderViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)
        with(holder.binding) {
            tvOrderRef.text = "Order #${order.orderRef}"
            tvItems.text = order.itemsSummary
            tvTotal.text = "₹${order.totalAmount.toInt()}"
            tvStatus.text = order.deliveryStatus
            tvPayment.text = "💳 ${order.paymentMethod}"

            // Format timestamp
            val sdf = SimpleDateFormat("MMM dd, yyyy · hh:mm a", Locale.getDefault())
            tvDate.text = sdf.format(Date(order.timestamp))
        }
    }
}
