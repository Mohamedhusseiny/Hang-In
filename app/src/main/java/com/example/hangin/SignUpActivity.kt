package com.example.hangin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hangin.authentication.APIClient
import com.example.hangin.authentication.APIInterface
import com.example.hangin.authentication.UserRegistration
import retrofit2.Call
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var imageViewBack: ImageView
    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmation: EditText
    private lateinit var buttonCreateAccount: Button

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var password: String
    private lateinit var confirmation: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        imageViewBack = findViewById(R.id.imageView_back)

        editTextFirstName = findViewById(R.id.editText_firstName)
        editTextLastName = findViewById(R.id.editText_lastName)
        editTextEmail = findViewById(R.id.signUp_editText_email)
        editTextPhone = findViewById(R.id.editText_phone)
        editTextPassword = findViewById(R.id.signUp_editText_password)
        editTextConfirmation = findViewById(R.id.editText_confirmation)
        buttonCreateAccount = findViewById(R.id.button_createAccount)
    }

    override fun onResume() {
        super.onResume()

        // Listener for navigating back to the login activity
        imageViewBack.setOnClickListener {
            val loginIntent = Intent(this, MainActivity::class.java)
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(loginIntent)
            finish()
        }

        // Listener for creating an account
        buttonCreateAccount.setOnClickListener {

            firstName = editTextFirstName.text.trim().toString()
            lastName = editTextLastName.text.trim().toString()
            email = editTextEmail.text.trim().toString()
            phone = editTextPhone.text.trim().toString()
            password = editTextPassword.text.trim().toString()
            confirmation = editTextConfirmation.text.trim().toString()

            if (checkValidationData(firstName, lastName, email, phone, password, confirmation)) {
                val userRegistration =
                    UserRegistration(firstName, lastName, email, phone, password)
                userRegister(userRegistration)
            }
        }
    }

    // TODO: Set red frame around plain texts
    private fun checkValidationData(
        firstName: String, lastName: String, email: String, phone: String,
        password: String, confirmation: String
    ): Boolean {
        var valid = true
        var msj = ""
        if (TextUtils.isEmpty(firstName)) {
            valid = false
            msj += "First Name is needed\n"
        } else if (TextUtils.isEmpty(lastName)) {
            valid = false
            msj += "Last Name is needed\n"
        } else if (TextUtils.isEmpty(phone)) {
            valid = false
            msj += "Phone number is needed\n"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false
            msj += "Email format is not correct\n"
        } else if (TextUtils.isEmpty(password)) {
            valid = false
            msj += "Password is empty\n"
        } else if (TextUtils.isEmpty(confirmation)) {
            valid = false
            msj += "Confirmation Password is empty\n"
        } else if (password != confirmation) {
            valid = false
            Toast.makeText(this, "Password doesn't match confirmation!", Toast.LENGTH_SHORT).show()
        }

        if (!TextUtils.isEmpty(msj)) {
            Toast.makeText(this, msj, Toast.LENGTH_LONG).show()
        }

        return valid
    }

    private fun userRegister(userRegistration: UserRegistration) {
        val service = APIClient.retrofit.create(APIInterface::class.java)
        val call = service.createUser(userRegistration)

        call.enqueue(object : retrofit2.Callback<UserRegistration> {
            override fun onFailure(call: Call<UserRegistration>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "Failed to register", Toast.LENGTH_LONG).show()
                call.cancel()
            }

            override fun onResponse(
                call: Call<UserRegistration>,
                response: Response<UserRegistration>
            ) {
                if (response.code() != 200) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Invalid Login Details \n Please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // TODO: Show Alert dialog & Proceed to Login
                    Toast.makeText(
                        this@SignUpActivity,
                        "Welcome!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
