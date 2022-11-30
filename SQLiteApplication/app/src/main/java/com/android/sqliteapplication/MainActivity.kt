package com.android.sqliteapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.android.sqliteapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dbHelper = MyDatabaseHelper(this, "BookStore.db", 2)
        binding.createDatabase.setOnClickListener {
            dbHelper.writableDatabase
        }

        binding.addData.setOnClickListener {
            val db = dbHelper.writableDatabase
            val value1 = ContentValues().apply {
                put("name", "The Da Vinci Code")
                put("author", "Dan Brown")
                put("price", 16.69)
                put("pages", 454)
            }
            db.insert("book", null, value1)
            val value2 = ContentValues().apply {
                put("name", "The Lost Symbol")
                put("author", "Dan Brown")
                put("price", 19.95)
                put("pages", 510)
            }
            db.insert("book", null, value2)
        }

        binding.updateData.setOnClickListener {
            val db = dbHelper.writableDatabase
            val values = ContentValues()
            values.put("price", 10.99)
            db.update("book", values, "name = ?", arrayOf("The Da Vinci Code"))
        }
        binding.deleteData.setOnClickListener {
            val db = dbHelper.writableDatabase
            db.delete("book", "pages > ?", arrayOf("500"))
        }
        binding.queryData.setOnClickListener {
            val db = dbHelper.writableDatabase
            val cursor = db.query("book", null, null, null, null, null, null)
            if(cursor.moveToFirst()){
                do {
                    val name = cursor.getStringOrNull(cursor.getColumnIndex("name"))
                    val author = cursor.getStringOrNull(cursor.getColumnIndex("author"))
                    val pages = cursor.getIntOrNull(cursor.getColumnIndex("pages"))
                    val price = cursor.getDoubleOrNull(cursor.getColumnIndex("price"))
                    Log.d("MainActivity","book name is $name")
                    Log.d("MainActivity","book author is $author")
                    Log.d("MainActivity","book pages is $pages")
                    Log.d("MainActivity","book price is $price")
                }while (cursor.moveToNext())
            }
            cursor.close()
        }
        binding.replaceData.setOnClickListener {
            val db = dbHelper.writableDatabase
            db.beginTransaction()
            try {
                db.delete("book", null, null)
                /*if(true){
                    throw NullPointerException()
                }*/
                val values = ContentValues().apply {
                    put("name", "Game of Thrones")
                    put("author", "George Martin")
                    put("pages", 720)
                    put("price", 20.85)
                }
                db.insert("book", null, values)
                db.setTransactionSuccessful()
            }catch (e : Exception){
                e.printStackTrace()
            }finally {
                db.endTransaction()
            }
        }
    }
}