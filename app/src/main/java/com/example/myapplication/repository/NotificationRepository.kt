package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.db.NotificationEntity
import com.example.myapplication.utils.NotificationHelper
import kotlinx.coroutines.flow.Flow

class NotificationRepository(private val context: Context) {

    private val notificationDao = AppDatabase.getInstance(context).notificationDao()

    fun getAllNotifications(): Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()

    suspend fun addNotification(title: String, message: String, type: String, actionData: String? = null) {
        val entity = NotificationEntity(
            title = title,
            message = message,
            type = type,
            actionData = actionData
        )
        notificationDao.insert(entity)

        // Also trigger a system notification
        if (type == "OFFER") {
            NotificationHelper.sendOfferNotification(context, title, message)
        } else {
            NotificationHelper.sendOrderUpdate(context, title, message)
        }
    }

    suspend fun markAllAsRead() = notificationDao.markAllAsRead()
    
    suspend fun deleteNotification(notification: NotificationEntity) = notificationDao.delete(notification)
}
