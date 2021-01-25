package com.gabrielaraujoz.firebasechallenge.accounts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.gabrielaraujoz.firebasechallenge.MainActivity
import com.gabrielaraujoz.firebasechallenge.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class CreateAccountActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var accountName: TextInputEditText
    private lateinit var accountEmail: TextInputEditText
    private lateinit var accountPassword: TextInputEditText
    private lateinit var accountRepeatPassword: TextInputEditText
    private lateinit var tilAccountName: TextInputLayout
    private lateinit var tilAccountEmail: TextInputLayout
    private lateinit var tilAccountPassword: TextInputLayout
    private lateinit var tilAccountRepeatPassword: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        auth = FirebaseAuth.getInstance()

        accountName = findViewById(R.id.etCreateName)
        accountEmail = findViewById(R.id.etCreateEmail)
        accountPassword = findViewById(R.id.etCreatePassword)
        accountRepeatPassword = findViewById(R.id.etRepeatPassword)
        tilAccountName = findViewById(R.id.tilCreateName)
        tilAccountEmail = findViewById(R.id.tilCreateEmail)
        tilAccountPassword = findViewById(R.id.tilCreatePassword)
        tilAccountRepeatPassword = findViewById(R.id.tilRepeatPassword)

        findViewById<Button>(R.id.btnCreateAcc).setOnClickListener() {
            if (validarCampos()) {
                criarConta(accountName.text.toString(), accountEmail.text.toString(), accountPassword.text.toString())
            }
        }

    }

    private fun validarCampos(): Boolean {
        var response = true

        if (accountEmail.text.isNullOrBlank()) {
            tilAccountEmail.error = "Please type your e-mail"
            response = false
        }

        if (accountName.text.isNullOrBlank()) {
            tilAccountName.error = "Please type your Name"
            response = false
        }

        if (accountPassword.text.isNullOrBlank()) {
            tilAccountPassword.error = "Password Required"
            response = false
        } else if (accountPassword.text!!.length < 8) {
            tilAccountPassword.error = "Password must be at least 8 characters long"
            response = false
        }

        if (accountRepeatPassword.text.isNullOrBlank()) {
            tilAccountRepeatPassword.error = "Please repeat your password"
            response = false
        } else if (accountPassword.text.toString() != accountRepeatPassword.text.toString()) {
            tilAccountPassword.error = "Passwords do not match"
            response = false
        }

        return response
    }

    private fun criarConta(nome: String, email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(
                                baseContext, "User successfully created",
                                Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("CREATE_ACCOUNT_FAIL", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }
}