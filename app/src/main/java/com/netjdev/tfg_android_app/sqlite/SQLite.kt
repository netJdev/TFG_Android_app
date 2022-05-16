package com.netjdev.tfg_android_app.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLite(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        // si no se indica la opción WITHOUT ROWID, SQLite añade un id autoincremental rowid
        // el rowid será la primary key
        db?.execSQL("create table notifications (name text, body text, date integer)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}