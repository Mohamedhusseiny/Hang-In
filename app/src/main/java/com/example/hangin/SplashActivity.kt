package com.example.hangin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.hangin.authentication.MainActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 3000
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        runnable = Runnable {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        handler = Handler()
        handler.postDelayed(runnable, SPLASH_DELAY)
    }
}
