package com.android.firstofall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        val button1 : Button = findViewById(R.id.button1)
        button1.setOnClickListener {
            Toast.makeText(this, "Button 1 clicked.", Toast.LENGTH_SHORT).show()
        }
    }
}