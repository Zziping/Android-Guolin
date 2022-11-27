package com.android.broadcastreceiverapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Toast.makeText(context, "Receive in myBroadcastReceiver", Toast.LENGTH_SHORT).show()
        abortBroadcast()
    }
}