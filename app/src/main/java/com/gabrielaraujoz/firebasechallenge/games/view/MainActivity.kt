package com.gabrielaraujoz.firebasechallenge.games.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gabrielaraujoz.firebasechallenge.R
import com.gabrielaraujoz.firebasechallenge.accounts.LoginActivity
import com.gabrielaraujoz.firebasechallenge.games.model.GameModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var storageRef: StorageReference
    private lateinit var databaseRef: DatabaseReference
    private lateinit var userDatabaseRef: DatabaseReference
    private lateinit var userStorageRef: StorageReference
    private lateinit var viewManager: GridLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: GameListAdapter
    private var gameList = mutableListOf<GameModel>()

    data class GameModelMain(
        val name: String = "",
        val created_at: String = "",
        val description: String = "",
        val image_URI: String = ""
    )

    private var game = GameModel("", "", "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialSetup()
        recyclerViewSetup()

        val gameListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach() {
                    val data = it.getValue(GameModelMain::class.java)!!
                    gameList.add(
                        GameModel(
                            data.name,
                            data.created_at,
                            data.description,
                            data.image_URI
                        )
                    )
                }
                viewAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", error.toException())
            }
        }

        userDatabaseRef.addValueEventListener(gameListener)

        findViewById<Button>(R.id.btnLogout).setOnClickListener() {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<FloatingActionButton>(R.id.btnAddGame).setOnClickListener() {
            val intent = Intent(this, CreateGameActivity::class.java)
            startActivity(intent)
        }

    }

    fun initialSetup() {
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
        storageRef = storage.getReference("uploads")
        databaseRef = database.getReference("users")
        userDatabaseRef = databaseRef.child(user.uid)
        userStorageRef = storageRef.child(user.uid)
    }

    fun recyclerViewSetup() {

        viewManager = GridLayoutManager(this, 2)
        recyclerView = findViewById(R.id.listGames)
        viewAdapter = GameListAdapter(gameList) {
            val intent = Intent(this, GameDetailsActivity::class.java)
            intent.putExtra("Name", it.name)
            intent.putExtra("Description", it.description)
            intent.putExtra("Created_at", it.created_at)
            intent.putExtra("ImageURI", it.image_URI)
            startActivity(intent)
        }

        recyclerView.apply {
            setHasFixedSize(true)

            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    companion object {
        const val TAG = "APP"
    }
}

