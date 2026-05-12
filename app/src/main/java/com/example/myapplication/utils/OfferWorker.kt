package com.example.myapplication.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.repository.NotificationRepository
import java.util.Random

class OfferWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val repository = NotificationRepository(applicationContext)
        
        val offers = listOf(
            "Flash Sale! ⚡ Up to 40% OFF on all Dairy products for the next 2 hours." to "Grab your milk, cheese and butter now!",
            "Weekend Special! 🥦 Fresh organic vegetables are now at lowest prices." to "Check out the new arrivals in the Fruits & Veggies section.",
            "Missing something? 🛒" to "Your cart is waiting! Complete your purchase now and get free delivery.",
            "New Arrival! 🍫" to "Premium dark chocolates are back in stock. Treat yourself today!"
        )

        val randomOffer = offers[Random().nextInt(offers.size)]

        // This will save to DB AND show a system notification (outside the app)
        repository.addNotification(
            randomOffer.first,
            randomOffer.second,
            "OFFER",
            "background_offer_${System.currentTimeMillis()}"
        )

        return Result.success()
    }
}
