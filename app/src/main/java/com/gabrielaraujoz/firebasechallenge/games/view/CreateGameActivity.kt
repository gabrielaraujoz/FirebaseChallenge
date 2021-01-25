package com.gabrielaraujoz.firebasechallenge.games.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
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

class CreateGameActivity : AppCompatActivity() {

    private lateinit var gameName: TextInputEditText
    private lateinit var gameDate: TextInputEditText
    private lateinit var gameDescription: TextInputEditText
    private lateinit var gameNameField: TextInputLayout
    private lateinit var gameDateField: TextInputLayout
    private lateinit var gameDescriptionField: TextInputLayout
    private lateinit var gameImage: CircleImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var userRef: StorageReference
    private lateinit var databaseRef: DatabaseReference
    private lateinit var imageFileReference: String
    private var imageURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_game)

        gameImage = findViewById(R.id.imgCreateGameBtn)
        gameImage.setOnClickListener() {
            procurarArquivo()
        }

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        storage = FirebaseStorage.getInstance()
        userRef = storage.getReference(user.uid)

        database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference(user.uid)

        gameName = findViewById(R.id.etGameCreateName)
        gameDate = findViewById(R.id.etGameCreateDate)
        gameDescription = findViewById(R.id.etGameCreateDescription)
        gameNameField = findViewById(R.id.tilGameCreateName)
        gameDateField = findViewById(R.id.tilGameCreateDate)
        gameDescriptionField = findViewById(R.id.tilGameCreateDescription)


        findViewById<MaterialButton>(R.id.btnCreateGame).setOnClickListener() {
            if (validarDados()) {
                enviarArquivo(userRef)
                enviarGame(
                    databaseRef,
                    gameName.text.toString(),
                    gameDate.text.toString(),
                    gameDescription.text.toString(),
                    imageFileReference
                )

            }
        }

    }

    fun validarDados(): Boolean {
        var response = true

        if (gameName.text.isNullOrBlank()) {
            gameNameField.error = getString(R.string.insert_game_name)
            response = false
        }

        if (gameDate.text.isNullOrBlank()) {
            gameDateField.error = getString(R.string.insert_game_launch_date)
            response = false
        }

        if (gameDescription.text.isNullOrBlank()) {
            gameDescriptionField.error = getString(R.string.insert_game_description)
            response = false
        }

        if (imageURI == null) {
            Toast.makeText(this, getString(R.string.insert_game_image), Toast.LENGTH_SHORT).show()
            response = false
        }

        return response
    }

    fun enviarArquivo(storageReference: StorageReference) {
        if (imageURI != null) {
            imageURI?.run {

                val extension = MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(contentResolver.getType(imageURI!!))

                val fileReference =
                    storageReference.child("${System.currentTimeMillis()}.${extension}")

                fileReference.putFile(this)
                    .addOnSuccessListener {
                        imageFileReference = fileReference.toString()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@CreateGameActivity,
                            getString(R.string.image_upload_failure),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
            }

        }
    }

    fun procurarArquivo() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, CONTENT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONTENT_REQUEST_CODE && resultCode == RESULT_OK) {
            imageURI = data?.data
            findViewById<CircleImageView>(R.id.imgCreateGameBtn).setImageURI(imageURI)
        }
    }

    fun enviarGame(
        databaseRef: DatabaseReference,
        name: String,
        date: String,
        description: String,
        imageRef: String
    ) {
        val newGame = GameModel(name, date, description, imageRef)
        databaseRef.child(name).setValue(newGame)

    }

    companion object {
        const val CONTENT_REQUEST_CODE = 1
    }
}