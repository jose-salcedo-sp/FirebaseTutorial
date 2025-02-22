package com.example.firebasetutorial

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        applyInsets()
        initializeFirebase()
        setupLoginButton()
    }

    override fun onStart() {
        super.onStart()
        checkUserSession()
    }

    private fun applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initializeFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun setupLoginButton() {
        findViewById<Button>(R.id.login).setOnClickListener {
            val email = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInUser(email, password)
            } else {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                showToast("User authenticated successfully")
                goMain()
            } else {
                showToast("Error: ${result.exception?.message ?: "Unknown error"}")
            }
        }
    }

    private fun checkUserSession() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            showToast("There is no authenticated user")
        } else {
            showToast("Hello ${currentUser.email}")
            goMain()
        }
    }

    private fun goMain() {
        auth.currentUser?.uid?.let { uid ->
            startActivity(Intent(this, MainActivity::class.java).putExtra("idUser", uid))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
