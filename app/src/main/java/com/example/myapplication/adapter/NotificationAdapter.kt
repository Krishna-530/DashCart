package com.example.myapplication.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemNotificationBinding
import com.example.myapplication.db.NotificationEntity

class NotificationAdapter(
    private var notifications: List<NotificationEntity>,
    private val onNotificationClick: (NotificationEntity) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(val binding: ItemNotificationBinding) : 
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.binding.apply {
            tvNotificationTitle.text = notification.title
            tvNotificationMessage.text = notification.message
            
            // Format time
            tvNotificationTime.text = DateUtils.getRelativeTimeSpanString(
                notification.timestamp,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            )

            // Icons based on type
            when (notification.type) {
                "OFFER" -> {
                    ivNotificationIcon.setImageResource(R.drawable.ic_discount)
                    flIconContainer.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFFF1F8E9.toInt())
                }
                "ORDER" -> {
                    ivNotificationIcon.setImageResource(R.drawable.ic_grocery_logo)
                    flIconContainer.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFFE3F2FD.toInt())
                }
                else -> {
                    ivNotificationIcon.setImageResource(R.drawable.ic_notifications)
                    flIconContainer.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFFF5F5F5.toInt())
                }
            }

            // Unread indicator
            vUnreadIndicator.visibility = if (notification.isRead) View.GONE else View.VISIBLE

            root.setOnClickListener { onNotificationClick(notification) }
        }
    }

    override fun getItemCount() = notifications.size

    fun updateList(newList: List<NotificationEntity>) {
        notifications = newList
        notifyDataSetChanged()
    }
}
