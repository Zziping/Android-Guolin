package com.android.activitylifecycletest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.activitylifecycletest.databinding.ActivityNormalBinding

class NormalActivity : AppCompatActivity() {
    lateinit var binding: ActivityNormalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNormalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}