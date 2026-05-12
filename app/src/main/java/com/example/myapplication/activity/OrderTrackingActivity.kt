package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityOrderTrackingBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * OrderTrackingActivity — Simulates real-time driver/order status.
 *
 * Stages auto-advance using a Handler every few seconds (demo-friendly timing).
 * In a real app, these would be pushed via WebSockets or FCM notifications.
 */
class OrderTrackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderTrackingBinding
    private val handler = Handler(Looper.getMainLooper())
    private var currentStage = 0

    // Simulated delays (ms) for each stage after the previous one
    private val stageDelays = longArrayOf(0, 5_000, 12_000, 20_000, 30_000, 45_000)

    private val stageIcons   = arrayOf("✅", "✅", "✅", "✅", "✅", "🎉")
    private val stageStatus  = arrayOf(
        "📦 Order placed — getting ready…",
        "✅ Order confirmed by store!",
        "📦 Your items are being packed…",
        "🛵 Driver Rahul has picked up your order!",
        "🛵 Rahul is on the way to your location!",
        "🎉 DELIVERED! Enjoy your order!"
    )

    private val iconViews  by lazy {
        listOf(binding.icon0, binding.icon1, binding.icon2,
               binding.icon3, binding.icon4, binding.icon5)
    }
    private val timeViews  by lazy {
        listOf(binding.time0, binding.time1, binding.time2,
               binding.time3, binding.time4, binding.time5)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        // Load active order or use current one
        val savedOrderId = com.example.myapplication.utils.SharedPrefsHelper.getActiveOrderId(this)
        val savedStartTime = com.example.myapplication.utils.SharedPrefsHelper.getActiveOrderTime(this)
        
        val orderId = savedOrderId ?: "GR${(10000..99999).random()}"
        val startTime = if (savedStartTime > 0) savedStartTime else System.currentTimeMillis()
        
        binding.tvOrderId.text = "Order #$orderId"

        startTracking(startTime)

        binding.btnBackHome.setOnClickListener {
            finish()
        }
    }

    private fun startTracking(startTime: Long) {
        // Calculate current stage based on elapsed time (for persistence)
        val elapsed = System.currentTimeMillis() - startTime
        
        // Find which stage we are currently at
        var totalDelay = 0L
        var initialStage = 0
        for (i in 0..5) {
            totalDelay += stageDelays[i]
            if (elapsed >= totalDelay) {
                initialStage = i
            }
        }

        // Catch up to current stage immediately
        for (i in 0..initialStage) {
            advanceTo(i, startTime + getCumulativeDelay(i))
        }

        // Schedule future stages
        for (i in (initialStage + 1)..5) {
            val delay = getCumulativeDelay(i) - elapsed
            val stage = i
            handler.postDelayed({ advanceTo(stage) }, if (delay > 0) delay else 0)
        }
    }

    private fun getCumulativeDelay(stage: Int): Long {
        var sum = 0L
        for (i in 0..stage) sum += stageDelays[i]
        return sum
    }

    private fun advanceTo(stage: Int, specificTime: Long = System.currentTimeMillis()) {
        if (stage <= currentStage && stage != 0) return // Already reached
        currentStage = stage
        
        val timeStr = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(specificTime))

        // Update UI
        for (i in 0..stage) {
            iconViews[i].text  = stageIcons[i]
            timeViews[i].text  = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(startTimeForStage(i)))
        }

        binding.tvCurrentStatus.text = stageStatus[stage]

        // Update Map Visualization (Feature 3)
        updateMapState(stage)

        // ── Feature 5: Sequential Notifications ─────────────────────────────
        when (stage) {
            1 -> com.example.myapplication.utils.NotificationHelper.sendOrderUpdate(this, "✅ Order Confirmed!", "The store has confirmed your order.")
            3 -> com.example.myapplication.utils.NotificationHelper.sendOrderUpdate(this, "🛵 Out for Delivery!", "Driver Rahul has picked up your order.")
            5 -> {
                com.example.myapplication.utils.NotificationHelper.sendOrderUpdate(this, "🎉 Delivered!", "Enjoy your groceries!")
                com.example.myapplication.utils.SharedPrefsHelper.clearActiveOrder(this)
                Toast.makeText(this, "🎉 Order Delivered! Enjoy!", Toast.LENGTH_LONG).show()
                binding.btnBackHome.visibility = View.VISIBLE
            }
        }
    }

    private fun startTimeForStage(stage: Int): Long {
        val base = com.example.myapplication.utils.SharedPrefsHelper.getActiveOrderTime(this)
        return if (base > 0) base + getCumulativeDelay(stage) else System.currentTimeMillis()
    }

    private fun updateMapState(stage: Int) {
        when (stage) {
            0, 1, 2 -> {
                binding.lottiePacking.visibility = View.VISIBLE
                binding.llRiderMarker.visibility = View.INVISIBLE
            }
            3 -> {
                binding.lottiePacking.visibility = View.GONE
                binding.llRiderMarker.visibility = View.VISIBLE
                binding.llRiderMarker.translationX = 0f
            }
            4 -> {
                binding.lottiePacking.visibility = View.GONE
                binding.llRiderMarker.visibility = View.VISIBLE
                // Animate rider to 70% of the way
                binding.llRiderMarker.animate()
                    .translationX(binding.cardMap.width * 0.5f)
                    .setDuration(15000)
                    .start()
            }
            5 -> {
                binding.lottiePacking.visibility = View.GONE
                binding.llRiderMarker.visibility = View.VISIBLE
                // Animate rider to the end
                binding.llRiderMarker.animate()
                    .translationX(binding.cardMap.width * 0.75f)
                    .setDuration(2000)
                    .start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
