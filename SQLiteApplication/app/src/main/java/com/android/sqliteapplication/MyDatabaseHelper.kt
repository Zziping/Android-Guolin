package com.android.sqliteapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import androidx.core.content.contentValuesOf

class MyDatabaseHelper(val context : Context, name : String, version : Int) : SQLiteOpenHelper(context, name, null, version) {
    private  val createBook = "create table book(" +
            "id integer primary key autoincrement," +
            "author text," +
            "price real," +
            "pages integer," +
            "name text)"
    private val createArea = "create table area(" +
            "id integer primary key autoincrement," +
            "name text," +
            "province text)"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createBook)
        db.execSQL(createArea)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion <= 1){
            db.execSQL(createArea)
        }
        if(oldVersion <= 2){
            db.execSQL("alter table book add column province text")
        }
    }
}