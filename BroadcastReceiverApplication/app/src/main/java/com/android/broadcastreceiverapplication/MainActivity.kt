package com.android.broadcastreceiverapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.broadcastreceiverapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var timeChangeReceiver : TimeChangeReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            val intent = Intent("com.android.broadcastreceiverapplication.MY_BROADCAST")
            intent.setPackage(packageName)
            sendOrderedBroadcast(intent, null)
        }
    }
    inner class TimeChangeReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "Time is changed", Toast.LENGTH_SHORT).show()
        }
    }
}