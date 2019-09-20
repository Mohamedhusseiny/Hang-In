package com.example.hangin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hangin.authentication.APIClient
import com.example.hangin.authentication.APIInterface
import com.example.hangin.authentication.UserLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var textViewSignUp: TextView
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignIn: Button

    private lateinit var email: String
    private lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewSignUp = findViewById(R.id.textView_signUp)
        editTextEmail = findViewById(R.id.editText_email)
        editTextPassword = findViewById(R.id.editText_password)
        buttonSignIn = findViewById(R.id.button_signIn)
    }

    override fun onResume() {
        super.onResume()

        textViewSignUp.setOnClickListener {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }
        buttonSignIn.setOnClickListener {
            email = editTextEmail.text.trim().toString()
            password = editTextPassword.text.trim().toString()
            if (checkValidationData(email, password)) {
                val user = UserLogin(email, password)
                userLogin(user)
            }
        }
    }

    // TODO: Set red borders
    private fun checkValidationData(email: String, password: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email format is incorrect!", Toast.LENGTH_SHORT).show()
            return false
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password is empty!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun userLogin(user: UserLogin) {
        val service = APIClient.retrofit.create(APIInterface::class.java)
        val call = service.checkUser(user)

        call.enqueue(object : Callback<UserLogin> {
            override fun onFailure(call: Call<UserLogin>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Cannot request",
                    Toast.LENGTH_SHORT
                ).show()
                call.cancel()
            }

            override fun onResponse(call: Call<UserLogin>, response: Response<UserLogin>) {
                if (response.code() == 200) {
                    // TODO: Use auth-token for user profile
                    // TODO: Open places activity
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Invalid username or password!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
    }
}
