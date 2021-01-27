package com.gabrielaraujoz.firebasechallenge.games.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.gabrielaraujoz.firebasechallenge.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class GameDetailsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var userStorageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details)

        val nameTop = findViewById<TextView>(R.id.txtGameView)
        val nameBottom = findViewById<TextView>(R.id.txtGameName)
        val launchDate = findViewById<TextView>(R.id.txtGameYear)
        val gameDescription = findViewById<TextView>(R.id.txtGameDescription)
        val gameImage = findViewById<ImageView>(R.id.imgGameView)

        nameTop.text = intent.getStringExtra("Name")
        nameBottom.text = intent.getStringExtra("Name")
        launchDate.text = intent.getStringExtra("Created_at")
        gameDescription.text = intent.getStringExtra("Description")

        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference("uploads")
/*        storageRef
        gameImage.setImageURI(storageRef.downloadUrl.result)*/


    }
}