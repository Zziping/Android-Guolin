package com.android.providerapplication

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.contentValuesOf
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.android.providerapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var bookId : String? = null
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addData.setOnClickListener {
            val uri = Uri.parse("content://com.android.sqliteapplication.provider/book")
            val values = contentValuesOf("name" to "A Clash of Kings", "author" to "George Martin", "pages" to 1040, "price" to 22.85)
            val newUri = contentResolver.insert(uri, values)
            bookId = newUri?.pathSegments?.get(1)
        }
        binding.queryData.setOnClickListener {
            val uri = Uri.parse("content://com.android.sqliteapplication.provider/book")
            contentResolver.query(uri, null, null, null, null)?.apply {
                while (moveToNext()){
                    val name = getStringOrNull(getColumnIndex("name"))
                    val author = getStringOrNull(getColumnIndex("author"))
                    val pages = getIntOrNull(getColumnIndex("pages"))
                    val price = getDoubleOrNull(getColumnIndex("price"))
                    Log.d("CPMainActivity", "book name is $name")
                    Log.d("CPMainActivity", "book author is $author")
                    Log.d("CPMainActivity", "book pages is $pages")
                    Log.d("CPMainActivity", "book price is $price")
                }
                close()
            }
        }
        binding.updateData.setOnClickListener {
            bookId?.let {
                val uri = Uri.parse("content://com.android.sqliteapplication.provider/book/$it")
                val values = contentValuesOf("name" to "A Storm of Swords", "pages" to 1216, "price" to 24.05)
                contentResolver.update(uri, values, null, null)
            }
        }
        binding.deleteData.setOnClickListener {
            bookId?.let {
                val uri = Uri.parse("content://com.android.sqliteapplication.provider/book/$it")
                contentResolver.delete(uri, null, null)
            }
        }
    }
}