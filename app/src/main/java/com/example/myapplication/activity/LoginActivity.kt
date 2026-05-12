package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginBinding

/**
 * LoginActivity — Entry point of the app.
 *
 * Flow:
 *  1. User enters a 10-digit mobile number and taps "Send OTP"
 *  2. OTP field becomes visible and a fake OTP hint is shown
 *  3. User enters "1234" and taps "Verify & Login"
 *  4. On success, navigate to HomeActivity
 *
 * No Firebase / network call — this is a purely local demo.
 */
class LoginActivity : AppCompatActivity() {

    // ViewBinding gives us type-safe access to all views in activity_login.xml
    private lateinit var binding: ActivityLoginBinding

    // Tracks whether the OTP has been "sent" (i.e., user already tapped Send OTP)
    private var otpSent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Check if already logged in
        if (com.example.myapplication.utils.SharedPrefsHelper.isLoggedIn(this)) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {

        // ── Send OTP button ──────────────────────────────────────────────────
        binding.btnSendOtp.setOnClickListener {
            val mobile = binding.etMobile.text.toString().trim()

            // Validate: mobile must be exactly 10 digits
            if (mobile.isEmpty()) {
                binding.etMobile.error = "Please enter your mobile number"
                return@setOnClickListener
            }
            if (mobile.length != 10) {
                binding.etMobile.error = "Enter a valid 10-digit mobile number"
                return@setOnClickListener
            }

            // Simulate sending OTP — show OTP section
            otpSent = true
            binding.layoutOtp.visibility = android.view.View.VISIBLE
            binding.btnVerify.visibility  = android.view.View.VISIBLE
            Toast.makeText(this, "OTP sent! Use 1234 to login.", Toast.LENGTH_SHORT).show()
        }

        // ── Verify & Login button ────────────────────────────────────────────
        binding.btnVerify.setOnClickListener {
            if (!otpSent) {
                Toast.makeText(this, "Please request OTP first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val otp = binding.etOtp.text.toString().trim()

            // Validate OTP is not empty
            if (otp.isEmpty()) {
                binding.etOtp.error = "Please enter the OTP"
                return@setOnClickListener
            }

            // Check fake OTP value
            if (otp != "1234") {
                binding.etOtp.error = "Invalid OTP. Use 1234"
                return@setOnClickListener
            }

            // OTP is correct — save login state and navigate to Home screen
            com.example.myapplication.utils.SharedPrefsHelper.setLoggedIn(this, true)
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Remove LoginActivity from back stack so user can't go back
        }
    }
}
