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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EditActivity : AppCompatActivity() {
    val database = Firebase.database
    val myRef = database.getReference("movies")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val params = intent.extras

        val nameField = findViewById<EditText>(R.id.edtTxtName_EditFrag)
        val yearField = findViewById<EditText>(R.id.edtTxtYear_EditFrag)
        val genreField = findViewById<EditText>(R.id.edtTxtGenre_EditFrag)
        val imgField = findViewById<EditText>(R.id.edtTxtImage_EditFrag)

        nameField.setText(params?.getCharSequence("name").toString())
        yearField.setText(params?.getCharSequence("year").toString())
        genreField.setText(params?.getCharSequence("genre").toString())
        imgField.setText(params?.getCharSequence("img").toString())


        val btnCancel = findViewById<Button>(R.id.butCancel_EditFrag)
        btnCancel.setOnClickListener {
            startActivity(Intent(this, MainActivity:: class.java))
            finish()
        }

        val btnEdit = findViewById<Button>(R.id.butEdit_EditFrag)
        btnEdit.setOnClickListener {
            var movie = MovieAux(nameField.text.toString(),
                yearField.text.toString(),
                genreField.text.toString(),
                imgField.text.toString(),
                "")
            myRef.child(params?.getCharSequence("id").toString()).setValue(movie).addOnCompleteListener{
                task->
                if(task.isSuccessful){
                    Toast.makeText(this, "Edited Movie", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity:: class.java))
                    finish()
                }else{
                    Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}