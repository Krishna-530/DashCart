package com.example.myapplication.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity)

    @Update
    suspend fun update(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Delete
    suspend fun delete(notification: NotificationEntity)

    @Query("DELETE FROM notifications")
    suspend fun deleteAll()
}
