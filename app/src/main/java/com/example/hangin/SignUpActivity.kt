package com.example.hangin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var imageViewBack: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        imageViewBack = findViewById(R.id.imageView_back)
    }

    override fun onResume() {
        super.onResume()

        imageViewBack.setOnClickListener {
            val loginIntent = Intent(this, MainActivity::class.java)
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(loginIntent)
            finish()
        }
    }
}
