package com.example.myapplication

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapplication.utils.DummyData
import com.example.myapplication.utils.NotificationHelper
import com.example.myapplication.utils.SharedPrefsHelper
import com.example.myapplication.utils.StockManager
import androidx.work.*
import com.example.myapplication.utils.OfferWorker
import java.util.concurrent.TimeUnit

/**
 * Application class — the first thing that runs before any Activity.
 *
 * Responsibilities:
 *  - Apply dark mode before any UI renders
 *  - Seed StockManager with initial stock counts (Feature 3)
 *  - Create notification channel for order updates (Feature 5)
 */
class GroceryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Apply the user's dark mode preference before any Activity is created
        if (SharedPrefsHelper.isDarkMode(this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Feature 3: Seed stock counts from product catalogue
        StockManager.init(DummyData.getProducts())

        // Feature 5: Create notification channels
        NotificationHelper.createChannels(this)

        // Schedule background offer checks
        scheduleOfferWorker()
    }

    private fun scheduleOfferWorker() {
        val workRequest = PeriodicWorkRequestBuilder<OfferWorker>(3, TimeUnit.HOURS)
            .setConstraints(Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build())
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "offer_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
