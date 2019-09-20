package com.example.hangin.authentication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.hangin.R
import retrofit2.Call
import retrofit2.Response


class SignUpActivity : AppCompatActivity() {

    private val ERROR_CODE = 0
    private val INVALID_CODE = 1
    private var imageEnablePassword = 0
    private var imageEnableConfirmation = 0

    private lateinit var imageViewBack: ImageView
    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmation: EditText
    private lateinit var buttonCreateAccount: Button
    private lateinit var imageViewEyePassowrd: ImageView
    private lateinit var imageViewEyeConfirmation: ImageView
    private lateinit var progressBar: ProgressBar

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var password: String
    private lateinit var confirmation: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Set up views
        imageViewBack = findViewById(R.id.imageView_back)

        editTextFirstName = findViewById(R.id.editText_firstName)
        editTextLastName = findViewById(R.id.editText_lastName)
        editTextEmail = findViewById(R.id.signUp_editText_email)
        editTextPhone = findViewById(R.id.editText_phone)
        editTextPassword = findViewById(R.id.signUp_editText_password)
        editTextConfirmation = findViewById(R.id.editText_confirmation)
        buttonCreateAccount = findViewById(R.id.button_createAccount)
        imageViewEyePassowrd = findViewById(R.id.imageView_eye_password)
        imageViewEyeConfirmation = findViewById(R.id.imageView_eye_confirmation)
        progressBar = findViewById(R.id.signUp_progressBar)
    }

    override fun onResume() {
        super.onResume()

        imageViewEyePassowrd.setOnClickListener {
            if (imageEnablePassword == 0) {
                imageViewEyePassowrd.setImageResource(R.drawable.ic_eye_activated)
                editTextPassword.transformationMethod = null
                imageEnablePassword = 1
                editTextPassword.setSelection(editTextPassword.text.lastIndex + 1)
            } else {
                imageViewEyePassowrd.setImageResource(R.drawable.ic_eye_deactivated)
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                imageEnablePassword = 0
                editTextPassword.setSelection(editTextPassword.text.lastIndex + 1)
            }
        }
        imageViewEyeConfirmation.setOnClickListener {
            if (imageEnableConfirmation == 0) {
                imageViewEyeConfirmation.setImageResource(R.drawable.ic_eye_activated)
                editTextConfirmation.transformationMethod = null
                imageEnableConfirmation = 1
                editTextConfirmation.setSelection(editTextConfirmation.text.lastIndex + 1)
            } else {
                imageViewEyeConfirmation.setImageResource(R.drawable.ic_eye_deactivated)
                editTextConfirmation.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                imageEnableConfirmation = 0
                editTextConfirmation.setSelection(editTextConfirmation.text.lastIndex + 1)
            }
        }

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
                progressBar.visibility = View.VISIBLE
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                val userRegistration =
                    UserRegistration(firstName, lastName, email, phone, password)
                userRegister(userRegistration)
            }
        }

        editTextPassword.addTextChangedListener {
            imageViewEyePassowrd.visibility = View.VISIBLE
        }
        editTextConfirmation.addTextChangedListener {
            imageViewEyeConfirmation.visibility = View.VISIBLE
        }
    }

    private fun checkValidationData(
        firstName: String, lastName: String, email: String, phone: String,
        password: String, confirmation: String
    ): Boolean {
        var valid = true
        if (isEmpty(firstName)) {
            editTextFirstName.error = getString(R.string.first_name_requirement)
            valid = false
        }
        if (isEmpty(lastName)) {
            editTextLastName.error = getString(R.string.last_name_requirement)
            valid = false
        }
        if (isEmpty(phone)) {
            editTextPhone.error = getString(R.string.phone_requirement)
            valid = false
        }
        if (isEmptyEmail(email)) {
            editTextEmail.error = getString(R.string.email_requirement)
            valid = false
        }
        if (isEmpty(password)) {
            editTextPassword.error = getString(R.string.password_requirement)
            imageViewEyePassowrd.visibility = View.GONE
            valid = false
        }
        if (isEmpty(confirmation)) {
            editTextConfirmation.error = getString(R.string.password_requirement)
            imageViewEyeConfirmation.visibility = View.GONE
            valid = false
        }
        if (password != confirmation) {
            editTextPassword.error = ""
            editTextConfirmation.error = ""
            imageViewEyePassowrd.visibility = View.GONE
            imageViewEyeConfirmation.visibility = View.GONE
            valid = false
        }
        return valid
    }

    private fun isEmptyEmail(email: String) = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun isEmpty(field: String) = TextUtils.isEmpty(field)

    private fun userRegister(userRegistration: UserRegistration) {
        val service = APIClient.retrofit.create(APIInterface::class.java)
        val call = service.createUser(userRegistration)

        call.enqueue(object : retrofit2.Callback<UserRegistration> {
            override fun onFailure(call: Call<UserRegistration>, t: Throwable) {
                progressBar.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                showAlert(ERROR_CODE)
                call.cancel()
            }

            override fun onResponse(
                call: Call<UserRegistration>, response: Response<UserRegistration>
            ) {
                progressBar.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                if (response.code() == 200) showProceedAlert()
                else showAlert(INVALID_CODE)
            }
        })
    }

    private fun showAlert(errorCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alert))
        if (errorCode == ERROR_CODE) builder.setMessage(getString(R.string.error))
        else builder.setMessage(getString(R.string.already_registered))
        builder.setIcon(resources.getDrawable(R.drawable.ic_alert))
        builder.setNeutralButton(getString(R.string.proceed), null)

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun showProceedAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.congrats))
        builder.setMessage(getString(R.string.registered))
        builder.setIcon(resources.getDrawable(R.drawable.ic_correct))
        builder.setNeutralButton(getString(R.string.proceed)) { dialogInterface, which ->
            val loginIntent = Intent(this, MainActivity::class.java)
            startActivity(loginIntent)
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}
