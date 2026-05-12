package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapplication.databinding.ActivityProfileBinding
import com.example.myapplication.utils.SharedPrefsHelper

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadProfileData()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun loadProfileData() {
        binding.etName.setText(SharedPrefsHelper.getName(this))
        binding.etEmail.setText(SharedPrefsHelper.getEmail(this))
        binding.etMobileDisplay.setText(SharedPrefsHelper.getMobile(this))
        binding.etHouseNo.setText(SharedPrefsHelper.getHouseNo(this))
        binding.etStreet.setText(SharedPrefsHelper.getStreet(this))
        binding.etCity.setText(SharedPrefsHelper.getCity(this))
        binding.etPincode.setText(SharedPrefsHelper.getPincode(this))
        binding.switchDarkMode.isChecked = SharedPrefsHelper.isDarkMode(this)
    }

    private fun setupListeners() {
        binding.btnSaveProfile.setOnClickListener {
            SharedPrefsHelper.saveName(this, binding.etName.text.toString())
            SharedPrefsHelper.saveEmail(this, binding.etEmail.text.toString())
            SharedPrefsHelper.saveHouseNo(this, binding.etHouseNo.text.toString())
            SharedPrefsHelper.saveStreet(this, binding.etStreet.text.toString())
            SharedPrefsHelper.saveCity(this, binding.etCity.text.toString())
            SharedPrefsHelper.savePincode(this, binding.etPincode.text.toString())

            // Build full address for checkout pre-fill
            val fullAddress = buildString {
                append(binding.etHouseNo.text.toString())
                val street = binding.etStreet.text.toString()
                if (street.isNotEmpty()) append(", $street")
                val city = binding.etCity.text.toString()
                if (city.isNotEmpty()) append(", $city")
                val pin = binding.etPincode.text.toString()
                if (pin.isNotEmpty()) append(" - $pin")
            }
            SharedPrefsHelper.saveAddress(this, fullAddress)

            Toast.makeText(this, "✅ Profile saved!", Toast.LENGTH_SHORT).show()
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            SharedPrefsHelper.setDarkMode(this, isChecked)
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
            // Recreate to apply theme immediately
            recreate()
        }

        binding.cardOrderHistory.setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            SharedPrefsHelper.setLoggedIn(this, false)
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
