package com.android.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.android.fragment.databinding.ActivityMainBinding
import com.android.fragment.databinding.LeftFragmentBinding
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val leftFrag = supportFragmentManager.findFragmentById(R.id.leftFrag) as LeftFragment
        leftFrag.binding.button.setOnClickListener {
            Log.d("TestBtn", "clicked")
        }
    }
}