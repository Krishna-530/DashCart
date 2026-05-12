package com.example.myapplication.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.NotificationAdapter
import com.example.myapplication.databinding.ActivityNotificationBinding
import com.example.myapplication.viewmodel.NotificationViewModel

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        observeViewModel()
        
        // Mark all as read when user opens this screen
        viewModel.markAllRead()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
        
        binding.btnTriggerTest.setOnClickListener {
            viewModel.addOfferNotification(
                "🔥 Flash Sale!",
                "Get 40% OFF on all Bakery items for the next 1 hour. Grab your favorites now!",
                "test_offer_${System.currentTimeMillis()}"
            )
            android.widget.Toast.makeText(this, "Notification will appear in system tray", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        adapter = NotificationAdapter(emptyList()) { notification ->
            // Handle notification click if needed (e.g. go to order details or product)
        }
        binding.rvNotifications.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity)
            adapter = this@NotificationActivity.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.allNotifications.observe(this) { notifications ->
            if (notifications.isNullOrEmpty()) {
                binding.llEmptyState.visibility = View.VISIBLE
                binding.rvNotifications.visibility = View.GONE
            } else {
                binding.llEmptyState.visibility = View.GONE
                binding.rvNotifications.visibility = View.VISIBLE
                adapter.updateList(notifications)
            }
        }
    }
}
