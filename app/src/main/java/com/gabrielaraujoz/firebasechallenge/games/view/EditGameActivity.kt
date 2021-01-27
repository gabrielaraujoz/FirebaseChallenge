package com.gabrielaraujoz.firebasechallenge.games.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.bumptech.glide.Glide
import com.gabrielaraujoz.firebasechallenge.R
import com.gabrielaraujoz.firebasechallenge.games.model.GameModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

class EditGameActivity : AppCompatActivity() {

    private lateinit var gameName: TextInputEditText
    private lateinit var gameDate: TextInputEditText
    private lateinit var gameDescription: TextInputEditText
    private lateinit var gameNameField: TextInputLayout
    private lateinit var gameDateField: TextInputLayout
    private lateinit var gameDescriptionField: TextInputLayout
    private lateinit var gameImage: CircleImageView
    private lateinit var name: String
    private lateinit var date: String
    private lateinit var description: String
    private lateinit var imgUrl: String
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var userRef: StorageReference
    private lateinit var databaseRef: DatabaseReference
    private var imageFileReference: String = ""
    private var imageURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_game)


        name = intent.getStringExtra("Name").toString()
        date = intent.getStringExtra("Created_at").toString()
        description = intent.getStringExtra("Description").toString()
        imgUrl = intent.getStringExtra("ImageURI").toString()

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        storage = FirebaseStorage.getInstance()
        userRef = storage.getReference("uploads")

        database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference("users")

        gameName = findViewById(R.id.etGameEditName)
        gameDate = findViewById(R.id.etGameEditDate)
        gameDescription = findViewById(R.id.etGameEditDescription)
        gameNameField = findViewById(R.id.tilGameEditName)
        gameDateField = findViewById(R.id.tilGameEditDate)
        gameDescriptionField = findViewById(R.id.tilGameEditDescription)

        gameName.setText(name)
        gameDate.setText(date)
        gameDescription.setText(description)


        gameImage = findViewById(R.id.imgEditGameBtn)

        if (imgUrl.isNotEmpty()) {
            Glide.with(this).load(imgUrl).into(gameImage)
        } else {
            gameImage.setImageResource(R.drawable.ic_camera_foreground)
        }

        gameImage.setOnClickListener() {
            procurarArquivo()
        }

        findViewById<MaterialButton>(R.id.btnEditGame).setOnClickListener() {
            validarDados()
        }

    }

    private fun validarDados() {

        if (gameName.text.isNullOrBlank()) {
            gameNameField.error = getString(R.string.insert_game_name)
        } else if (gameDate.text.isNullOrBlank()) {
            gameDateField.error = getString(R.string.insert_game_launch_date)
        } else if (gameDescription.text.isNullOrBlank()) {
            gameDescriptionField.error = getString(R.string.insert_game_description)
        } else {
            enviarArquivo(userRef)
        }

    }

    private fun enviarArquivo(storageReference: StorageReference) {
        if (imageURI == null) {

            enviarGame(databaseRef,
                    gameName.text.toString(),
                    gameDate.text.toString(),
                    gameDescription.text.toString(),
                    imgUrl)

        } else {
            imageURI?.run {

                val extension = MimeTypeMap.getSingleton()
                        .getExtensionFromMimeType(contentResolver.getType(this))

                val fileReference =
                        storageReference.child(user.uid)
                                .child("${System.currentTimeMillis()}.${extension}")

                fileReference.putFile(this)
                        .addOnSuccessListener {
                            fileReference.downloadUrl.addOnSuccessListener {
                                imageFileReference = it.toString()
                                enviarGame(databaseRef,
                                        gameName.text.toString(),
                                        gameDate.text.toString(),
                                        gameDescription.text.toString(),
                                        imageFileReference)
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                    this@EditGameActivity,
                                    getString(R.string.image_upload_failure),
                                    Toast.LENGTH_SHORT
                            )
                                    .show()
                        }
            }
        }
    }

    private fun procurarArquivo() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, CONTENT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONTENT_REQUEST_CODE && resultCode == RESULT_OK) {
            imageURI = data?.data
            findViewById<CircleImageView>(R.id.imgEditGameBtn).setImageURI(imageURI)
        }
    }

    private fun enviarGame(
            databaseRef: DatabaseReference,
            name: String,
            date: String,
            description: String,
            imageRef: String
    ) {
        if (name == this.name) {
            val newGame = GameModel(name, date, description, imageRef)
            databaseRef.child(user.uid).child(name).setValue(newGame)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val newGame = GameModel(name, date, description, imageRef)
            databaseRef.child(user.uid).child(name).setValue(newGame)
            databaseRef.child(user.uid).child(this.name).removeValue()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    companion object {
        const val CONTENT_REQUEST_CODE = 1
    }
}