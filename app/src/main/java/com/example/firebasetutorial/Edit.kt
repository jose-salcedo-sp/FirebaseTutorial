package com.example.firebasetutorial

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Edit : AppCompatActivity() {

    private val goBackMain: Button by lazy { findViewById(R.id.goBackMain) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        goBackMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Evita múltiples instancias
            startActivity(intent)
            finish()
        }
    }
}
