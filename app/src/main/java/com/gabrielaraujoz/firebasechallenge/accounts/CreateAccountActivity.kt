package com.gabrielaraujoz.firebasechallenge.accounts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gabrielaraujoz.firebasechallenge.R

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        supportActionBar?.hide()

    }
}