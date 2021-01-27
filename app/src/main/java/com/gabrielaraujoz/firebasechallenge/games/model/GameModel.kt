package com.gabrielaraujoz.firebasechallenge.games.model

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

data class GameModel(
        var name: String = "",
        var created_at: String = "",
        var description: String = "",
        var image_URI: String = ""
)