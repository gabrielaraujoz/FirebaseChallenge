package com.gabrielaraujoz.firebasechallenge.accounts

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gabrielaraujoz.firebasechallenge.MainActivity
import com.gabrielaraujoz.firebasechallenge.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var accountEmail: TextInputEditText
    private lateinit var accountPassword: TextInputEditText
    private lateinit var tilAccountEmail: TextInputLayout
    private lateinit var tilAccountPassword: TextInputLayout
    private lateinit var checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        accountEmail = findViewById(R.id.etLoginEmail)
        accountPassword = findViewById(R.id.etLoginPassword)
        checkBox = findViewById(R.id.cbLoginRemember)

        findViewById<Button>(R.id.btnLoginCreateAcc).setOnClickListener() {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        findViewById<MaterialButton>(R.id.btnLoginLogin).setOnClickListener() {
            if (validarCampos()) {
                auth.signInWithEmailAndPassword(accountEmail.text.toString(), accountPassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success")
                            val user = auth.currentUser
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Email ou senha incorretos.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }


    private fun validarCampos(): Boolean {
        var response = true

        if (accountEmail.text.isNullOrBlank()) {
            tilAccountEmail.error = getString(R.string.type_email)
            response = false
        }


        if (accountPassword.text.isNullOrBlank()) {
            tilAccountPassword.error = getString(R.string.type_password)
            response = false
        }

        return response
    }

//    fun salvarRemember(context: Context, check: Boolean) {
//        val preferences: SharedPreferences =
//            context.getSharedPreferences("APP", Context.MODE_PRIVATE)
//        preferences.edit().putBoolean("REMEMBER", check).apply()
//    }
}