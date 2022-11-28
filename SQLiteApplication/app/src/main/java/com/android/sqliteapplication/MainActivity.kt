package com.android.sqliteapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.sqliteapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dbHelper = MyDatabaseHelper(this, "BookStore.db", 1)
        binding.createDatabase.setOnClickListener {
            dbHelper.writableDatabase
        }
    }
}