package com.example.firebasetutorial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database
    private val myRef = database.getReference("movies")
    private var data: ArrayList<Movies> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setupUI()
        initializeFirebase()
        readDB()
        loadMockData() // Load mock movies
    }

    private fun setupUI() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Toolbar>(R.id.toolbar).let { setSupportActionBar(it) }
    }

    private fun initializeFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> logout()
            R.id.profile -> showToast("Go to profile")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadMockData() {
        data = arrayListOf(
            Movies("Inception", "2010", "action", "asdasdasdas"),
            Movies("The Godfather", "1972", "drama", "idusaaiosda"),
            Movies("Interstellar", "2014", "action", "posadiasdoip"),
            Movies("Parasite", "2019", "comedy", "asodiasidoasud"),
            Movies("The Dark Knight", "2008", "action", "asdausidasuid"),
            Movies("The exorcist", "1973", "horror", "asdjahdsjhasl")
        )
        val list = findViewById<ListView>(R.id.list)
        list.adapter = MovieAdapter(this, data)
    }

    private fun logout() {
        auth.signOut()
        startActivity(Intent(this, Login::class.java))
        finish()
    }

    private fun fillList() {
        findViewById<ListView>(R.id.list).adapter = MovieAdapter(this, data)
    }

    private fun readDB() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                snapshot.children.forEach { child ->
                    val movie = Movies(
                        name = child.child("name").getValue(String::class.java) ?: "",
                        year = child.child("year").getValue(String::class.java) ?: "",
                        genre = child.child("genre").getValue(String::class.java) ?: "",
                        id = child.key ?: ""
                    )
                    data.add(movie)
                }
                fillList()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("FirebaseDB", "Failed to read value.", error.toException())
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
