package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySplashBinding

/**
 * SplashActivity — Feature 4: Lottie animated splash screen.
 *
 * Plays a 2-second Lottie animation (splash_animation.json in res/raw).
 * After the animation the user is forwarded to HomeActivity.
 * The back-stack is cleared so the user cannot navigate back here.
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    // Total splash display time (ms). Matches animation length (≈ 3 s at 24 fps for 72 frames).
    private val SPLASH_DURATION = 2500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set preloaded flag for HomeActivity to skip shimmer delay
        HomeActivity.isPreloaded = true

        // ── Magic Unboxing Animation ──
        
        // 1. Box Falls & Bounces
        binding.tvBox.animate()
            .translationY(0f)
            .setDuration(1000)
            .setInterpolator(android.view.animation.BounceInterpolator())
            .withEndAction {
                
                // 2. Box Shakes
                binding.tvBox.animate()
                    .rotation(10f)
                    .setDuration(100)
                    .setInterpolator(android.view.animation.CycleInterpolator(3f))
                    .withEndAction {
                        
                        // 3. EXPLOSION REVEAL
                        binding.tvBox.animate()
                            .alpha(0f)
                            .scaleX(2f) // Expand as it bursts
                            .scaleY(2f)
                            .setDuration(400)
                            .withEndAction { binding.tvBox.visibility = android.view.View.GONE }
                            .start()
                        
                        // Fly out Items (Circular Burst)
                        animateFlyingItem(binding.item1, -300f, -400f) // Top Left
                        animateFlyingItem(binding.item2, 300f, -400f)  // Top Right
                        animateFlyingItem(binding.item3, -400f, 0f)    // Left
                        animateFlyingItem(binding.item4, 400f, 0f)     // Right
                        animateFlyingItem(binding.item5, -300f, 400f)  // Bottom Left
                        animateFlyingItem(binding.item6, 300f, 400f)   // Bottom Right

                        // Reveal Brand Content
                        binding.llSplashContent.animate()
                            .alpha(1f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(1000)
                            .setInterpolator(android.view.animation.OvershootInterpolator())
                            .start()
                    }
                    .start()
            }
            .start()

        // 4. Final Navigation
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isFinishing) {
                startActivity(Intent(this, HomeActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }, 4500L)
    }

    private fun animateFlyingItem(view: android.view.View, tx: Float, ty: Float) {
        view.alpha = 1f
        view.animate()
            .translationX(tx)
            .translationY(ty)
            .rotation(360f)
            .alpha(0f)
            .setDuration(1200)
            .setInterpolator(android.view.animation.DecelerateInterpolator())
            .start()
    }
}
