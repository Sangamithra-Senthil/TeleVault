package com.example.televault.View.Splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.televault.View.HomePage.MainActivity
import com.example.televault.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        val logoImageView: ImageView = findViewById(R.id.logoImageView)
        val zoomInAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        logoImageView.startAnimation(zoomInAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000) // 3 seconds delay
    }
}