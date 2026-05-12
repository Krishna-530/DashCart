package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.db.NotificationEntity
import com.example.myapplication.repository.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NotificationRepository(application)
    val allNotifications = repository.getAllNotifications().asLiveData()

    fun addOfferNotification(title: String, message: String, offerId: String? = null) {
        viewModelScope.launch {
            repository.addNotification(title, message, "OFFER", offerId)
        }
    }

    fun markAllRead() {
        viewModelScope.launch {
            repository.markAllAsRead()
        }
    }
    
    fun deleteNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            repository.deleteNotification(notification)
        }
    }
}
