package com.example.firebasetutorial

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private lateinit var moviesViewModel: MoviesViewModel

    val database = Firebase.database
    val myRef = database.getReference("movies")

    lateinit var toolbar : Toolbar

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            awarded ->
        if (awarded){
            Toast.makeText(this,"Permissions Awarded", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "Permissions Denied", Toast.LENGTH_LONG).show()
        }
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        toolbar = findViewById(R.id.toolbar_Main)
        setSupportActionBar(toolbar)


        auth = FirebaseAuth.getInstance()
        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        readDB()
    }

    private fun readDB() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val movies = ArrayList<Movie>()
                snapshot.children.forEach { child ->
                    val movie = Movie(
                        child.child("name").value.toString(),
                        child.child("year").value.toString(),
                        child.child("genre").value.toString(),
                        child.child("img").value.toString(),
                        child.child("location").value.toString(),
                        child.key.toString()
                    )
                    movies.add(movie)
                }
                moviesViewModel.updateMoviesList(movies) // Updating ViewModel
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_nav)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menuItem_Exit){
            auth.signOut()
            startActivity(Intent(this, LoginActivity:: class.java))
            finish()
        }
        else if (item.itemId == R.id.menuItem_Profile){}
        return super.onOptionsItemSelected(item)
    }

    private fun changeFragment(fragment: Fragment){
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView_Main, fragment)
        transaction.commit()
    }

    fun getLocation(callback: (String) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            requestPermissionLauncher.launch(permissions[0])
            requestPermissionLauncher.launch(permissions[1])
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val currentLocation = "${location.latitude} ${location.longitude}"
                    callback(currentLocation)
                } else {
                    callback("Location not found")
                }
            }
    }



    fun changeToViewMovies(){
        changeFragment(ViewMoviesFragment())
    }

    fun changeToAddMovies(){
        changeFragment(AddMoviesFragment())
    }
}