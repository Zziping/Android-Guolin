package com.android.activitylifecycletest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.activitylifecycletest.databinding.ActivityDialogBinding

class DialogActivity : AppCompatActivity() {
    lateinit var binding: ActivityDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}