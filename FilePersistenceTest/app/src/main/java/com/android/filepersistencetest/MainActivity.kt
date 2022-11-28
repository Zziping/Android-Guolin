package com.android.filepersistencetest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.filepersistencetest.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val inputText = load()
        if(inputText.isNotEmpty()){
            binding.editText.setText(inputText)
            binding.editText.setSelection((inputText.length))
            Toast.makeText(this, "succeed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val inputText = binding.editText.text.toString()
        save(inputText)
    }

    private fun load() : String{
        val content = StringBuilder()
        try{
            //获取FileInputStream对象
            val input = openFileInput("data")
            //构建InputStreamReader对象，再构建BufferedReader对象
            val reader = BufferedReader(InputStreamReader(input))
            //通过BufferedReader对象将文件中的数据一行一行读取出来
            reader.use {
                reader.forEachLine {
                    content.append(it)
                }
            }
        }catch (e : IOException){
            e.printStackTrace()
        }
        return content.toString()
    }

    private fun save(inputText : String){
        try {
            val output = openFileOutput("data", Context.MODE_PRIVATE)
            val writer = BufferedWriter(OutputStreamWriter(output))
            writer.use {
                it.write(inputText)
            }
        }catch (e : IOException){
            e.printStackTrace()
        }
    }
}