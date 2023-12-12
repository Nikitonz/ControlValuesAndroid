package com.nikitonz.controlvalues

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        const val DATABASE_NAME = "CommonDB.db"
        const val TABLE_USERS = "users"
        const val TABLE_COMMONDATA = "data"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(UserContract.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(UserContract.SQL_DELETE_TABLE)
        onCreate(db)
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        if (!checkDatabase()) {
            Toast.makeText(context, "db not exists. creating", Toast.LENGTH_SHORT).show()
            super.getWritableDatabase()
        }
        return super.getReadableDatabase()
    }

    private fun checkDatabase(): Boolean {
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        return dbFile.exists()
    }
}