package com.example.hangin.authentication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.hangin.PlaceActivity
import com.example.hangin.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val ERROR_CODE = 0
    private val INVALID_CODE = 1
    private var imageEnable = 0

    private lateinit var textViewSignUp: TextView
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignIn: Button
    private lateinit var imageViewEye: ImageView
    private lateinit var progressBar: ProgressBar

    private lateinit var email: String
    private lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up views
        textViewSignUp = findViewById(R.id.textView_signUp)
        editTextEmail = findViewById(R.id.editText_email)
        editTextPassword = findViewById(R.id.editText_password)
        buttonSignIn = findViewById(R.id.button_signIn)
        imageViewEye = findViewById(R.id.imageView_eye)
        progressBar = findViewById(R.id.main_progressBar)
    }

    override fun onResume() {
        super.onResume()

        textViewSignUp.setOnClickListener {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }
        imageViewEye.setOnClickListener {
            if (imageEnable == 0) {
                imageViewEye.setImageResource(R.drawable.ic_eye_activated)
                editTextPassword.transformationMethod = null
                imageEnable = 1
                editTextPassword.setSelection(editTextPassword.text.lastIndex + 1)
            } else {
                imageViewEye.setImageResource(R.drawable.ic_eye_deactivated)
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                imageEnable = 0
                editTextPassword.setSelection(editTextPassword.text.lastIndex + 1)
            }
        }
        buttonSignIn.setOnClickListener {
            email = editTextEmail.text.trim().toString()
            password = editTextPassword.text.trim().toString()
            if (checkDataValidation(email, password)) {
                progressBar.visibility = View.VISIBLE
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                val user = UserLogin(email, password)
                userLogin(user)
            }
        }

        editTextPassword.addTextChangedListener {
            imageViewEye.visibility = View.VISIBLE
        }
    }

    private fun checkDataValidation(email: String, password: String): Boolean {
        var valid = true
        if (isEmptyEmail(email)) {
            editTextEmail.error = getString(R.string.email_requirement)
            valid = false
        }
        if (isEmpty(password)) {
            editTextPassword.error = getString(R.string.password_requirement)
            imageViewEye.visibility = View.GONE
            valid = false
        }
        return valid
    }

    private fun isEmptyEmail(email: String) = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun isEmpty(field: String) = TextUtils.isEmpty(field)

    private fun userLogin(user: UserLogin) {
        val service = APIClient.retrofit.create(APIInterface::class.java)
        val call = service.checkUser(user)

        call.enqueue(object : Callback<UserLogin> {
            override fun onFailure(call: Call<UserLogin>, t: Throwable) {
                progressBar.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                showErrorAlert(ERROR_CODE)
                call.cancel()
            }

            override fun onResponse(call: Call<UserLogin>, response: Response<UserLogin>) {
                progressBar.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                if (response.code() == 200) {
                    // TODO: Use auth-token for user profile
                    val placeIntent = Intent(this@MainActivity, PlaceActivity::class.java)
                    startActivity(placeIntent)
                } else showErrorAlert(INVALID_CODE)
            }
        })
    }

    private fun showErrorAlert(errorCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alert))
        if (errorCode == 0) builder.setMessage(getString(R.string.error))
        else builder.setMessage(getString(R.string.invalid))
        builder.setIcon(resources.getDrawable(R.drawable.ic_alert))
        builder.setNeutralButton(getString(R.string.proceed), null)

        val alertDialog = builder.create()
        alertDialog.show()
    }
}
