package com.gabrielaraujoz.firebasechallenge.games.model

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

data class GameModel(
    val name: String,
    val created_at: String,
    val description: String,
    val image_URI: String
)