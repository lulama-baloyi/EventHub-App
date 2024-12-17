package com.example.EventHub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, SuccessActivity::class.java))
            finish()
        }, 3000) // Simulate 3 seconds loading time
    }
}