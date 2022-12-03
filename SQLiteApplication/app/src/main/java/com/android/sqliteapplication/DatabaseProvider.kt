package com.android.sqliteapplication

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class DatabaseProvider : ContentProvider() {
    private val bookDir = 0
    private val bookItem = 1
    private val areaDir = 2
    private val areaItem = 3
    private val authority = "com.android.sqliteapplication.provider"
    private var dbHelper : MyDatabaseHelper? = null
    private val uriMatcher by lazy {
        val matcher = UriMatcher(UriMatcher.NO_MATCH)
        matcher.addURI(authority, "book", bookDir)
        matcher.addURI(authority, "book/#", bookItem)
        matcher.addURI(authority, "area", areaDir)
        matcher.addURI(authority, "area/#", areaItem)
        matcher
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = dbHelper?.let {
        val db = it.writableDatabase
        val deleteRows = when(uriMatcher.match(uri)){
            bookDir -> db.delete("book", selection, selectionArgs)
            bookItem -> {
                val bookId = uri.pathSegments[1]
                db.delete("book", "id = ?", arrayOf(bookId))
            }
            areaDir -> db.delete("area", selection, selectionArgs)
            areaItem -> {
                val areaId = uri.pathSegments[1]
                db.delete("area", "id = ?", arrayOf(areaId))
            }
            else -> 0
        }
        deleteRows
    } ?: 0
    override fun getType(uri: Uri) = when(uriMatcher.match(uri)){
        bookDir -> "vnd.android.cursor.dir/vnd.com.android.sqliteapplication.provider.book"
        bookItem -> "vnd.android.cursor.item/vnd.com.android.sqliteapplication.provider.book"
        areaDir -> "vnd.android.cursor.dir/vnd.com.android.sqliteapplication.provider.area"
        areaItem -> "vnd.android.cursor.item/vnd.com.android.sqliteapplication.provider.area"
        else -> null
    }

    override fun insert(uri: Uri, values: ContentValues?) = dbHelper?.let {
        val db = it.writableDatabase
        val uriReturn = when(uriMatcher.match(uri)){
            bookDir, bookItem -> {
                val newBookId = db.insert("book", null, values)
                Uri.parse("content://$authority/book/$newBookId")
            }
            areaDir, areaItem -> {
                val newAreaId = db.insert("area", null, values)
                Uri.parse("content://$authority/area/$newAreaId")
            }
            else -> null
        }
        uriReturn
    }
    override fun onCreate() = context?.let {
        dbHelper = MyDatabaseHelper(it, "BookStore.db", 2)
        true
    } ?: false

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?) = dbHelper?.let {
        //查询数据
        val db = it.readableDatabase
        val cursor = when(uriMatcher.match(uri)){
            bookDir -> db.query("book", projection, selection, selectionArgs, null, null, sortOrder)
            bookItem -> {
                val bookId = uri.pathSegments[1]
                db.query("book", projection, "id = ?", arrayOf(bookId), null, null, sortOrder)
            }
            areaDir -> db.query("area", projection, selection, selectionArgs, null, null, sortOrder)
            areaItem -> {
                val areaId = uri.pathSegments[1]
                db.query("area", projection, "id = ?", arrayOf(areaId), null, null, sortOrder)
            }
            else -> null
        }
        cursor
    }
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?) = dbHelper?.let {
        val db = it.writableDatabase
        val updateRows = when(uriMatcher.match(uri)){
            bookDir -> db.update("book", values, selection, selectionArgs)
            bookItem -> {
                val bookId = uri.pathSegments[1]
                db.update("book", values, "id = ?", arrayOf(bookId))
            }
            areaDir -> db.update("area", values, selection, selectionArgs)
            areaItem -> {
                val areaId = uri.pathSegments[1]
                db.update("area", values, "id = ?", arrayOf(areaId))
            }
            else -> 0
        }
        updateRows
    } ?: 0
}