package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.OrderHistoryAdapter
import com.example.myapplication.databinding.ActivityOrderHistoryBinding
import com.example.myapplication.viewmodel.OrderViewModel

/**
 * OrderHistoryActivity — Feature 2: displays all past orders from Room DB.
 *
 * Orders are shown in descending chronological order.
 * An empty-state view is shown when no orders exist.
 */
class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderHistoryBinding
    private val viewModel: OrderViewModel by viewModels()
    private lateinit var adapter: OrderHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        observeOrders()
        setupEmptyAction()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Order History"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupRecyclerView() {
        adapter = OrderHistoryAdapter()
        binding.rvOrderHistory.layoutManager = LinearLayoutManager(this)
        binding.rvOrderHistory.adapter = adapter
    }

    private fun observeOrders() {
        viewModel.allOrders.observe(this) { orders ->
            if (orders.isNullOrEmpty()) {
                binding.rvOrderHistory.visibility = View.GONE
                binding.layoutEmptyState.visibility = View.VISIBLE
            } else {
                binding.rvOrderHistory.visibility = View.VISIBLE
                binding.layoutEmptyState.visibility = View.GONE
                adapter.submitList(orders)
            }
        }
    }

    private fun setupEmptyAction() {
        binding.btnShopNow.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}
