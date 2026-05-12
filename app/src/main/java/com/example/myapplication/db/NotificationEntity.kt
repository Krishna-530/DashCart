package com.example.myapplication.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * NotificationEntity — Represents a single notification item in the in-app notification center.
 */
@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String, // "OFFER", "ORDER", "SYSTEM"
    val isRead: Boolean = false,
    val actionData: String? = null // e.g., JSON or ID related to the action
)
