package com.example.EventHub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, CartActivity::class.java))
            finish()
        }, 3000) // Display for 3 minutes (180,000 milliseconds)
    }
}