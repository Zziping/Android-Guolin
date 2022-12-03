package com.android.contentprovidertest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.database.getStringOrNull
import com.android.contentprovidertest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val contactsList = ArrayList<String>()
    private lateinit var adapter : ArrayAdapter<String>
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactsList)
        binding.contactsView.adapter = adapter
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 1)
        }else{
            readContacts()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readContacts()
                }else{
                    Toast.makeText(this, "Permission Denial", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun readContacts(){
        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)?.apply {
            while (moveToNext()){
                val displayName = getStringOrNull(getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = getStringOrNull(getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contactsList.add("$displayName\n$number")
            }
            adapter.notifyDataSetChanged()
            close()
        }
    }
}