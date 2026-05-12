package com.example.myapplication.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R

/**
 * NotificationHelper — Manages all local notifications for the app.
 */
object NotificationHelper {

    private const val ORDER_CHANNEL_ID   = "order_channel"
    private const val OFFER_CHANNEL_ID   = "offer_channel"
    
    private const val ORDER_CHANNEL_NAME = "Order Updates"
    private const val OFFER_CHANNEL_NAME = "Offers & Promotions"
    
    private const val ORDER_CHANNEL_DESC = "Notifications about your grocery orders"
    private const val OFFER_CHANNEL_DESC = "Exclusive discounts and new arrivals"
    
    private const val NOTIFICATION_ID_BASE = 1000
    private const val OFFER_ID_BASE = 2000

    /**
     * Creates the notification channels required on Android 8+ (API 26).
     */
    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Order Channel
            val orderChannel = NotificationChannel(
                ORDER_CHANNEL_ID,
                ORDER_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = ORDER_CHANNEL_DESC
                enableLights(true)
                enableVibration(true)
            }
            manager.createNotificationChannel(orderChannel)

            // Offer Channel
            val offerChannel = NotificationChannel(
                OFFER_CHANNEL_ID,
                OFFER_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = OFFER_CHANNEL_DESC
                enableLights(true)
                lightColor = android.graphics.Color.GREEN
            }
            manager.createNotificationChannel(offerChannel)
        }
    }

    /**
     * Fires generic order update notifications.
     */
    fun sendOrderUpdate(context: Context, title: String, message: String, notificationId: Int = 1002) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) 
                != PackageManager.PERMISSION_GRANTED) return
        }

        val intent = Intent(context, com.example.myapplication.activity.OrderTrackingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, ORDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_grocery_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun sendOrderConfirmation(context: Context) {
        sendOrderUpdate(context, "🛒 Order Confirmed!", "Your order is confirmed! Arriving in 30 mins.", NOTIFICATION_ID_BASE + 1)
    }

    /**
     * Fires a promotional offer notification.
     */
    fun sendOfferNotification(context: Context, title: String, message: String, offerId: Int = 1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) 
                != PackageManager.PERMISSION_GRANTED) return
        }

        val intent = Intent(context, com.example.myapplication.activity.HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("EXTRA_OFFER_ID", offerId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            OFFER_ID_BASE + offerId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, OFFER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_grocery_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, R.color.primary_green))
            .build()

        try {
            NotificationManagerCompat.from(context).notify(OFFER_ID_BASE + offerId, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
