package com.nikitonz.controlvalues

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.nikitonz.controlvalues.UserContract
import java.io.File

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        const val DATABASE_NAME = "CommonDB.db"
        const val TABLE_USERS = "users"
        const val TABLE_COMMONDATA = "data"

        // Добавим константы для пользователя authority@root
        const val DEFAULT_USERNAME = "authority@root"
        const val DEFAULT_PASSWORD = "33223311"
    }

    override fun onCreate(db: SQLiteDatabase) {
        if (!isTableExists(TABLE_USERS, db)) {
            db.execSQL(UserContract.SQL_CREATE_TABLE)
        }

        db.beginTransaction()
        try {
            if (!isUserExists(DEFAULT_USERNAME, db)) {
                insertUser(DEFAULT_USERNAME, DEFAULT_PASSWORD, db)
                db.setTransactionSuccessful() // Установите успешность транзакции после успешной вставки
            }
        } finally {
            db.endTransaction()
        }
    }

    private fun isTableExists(tableName: String, db: SQLiteDatabase): Boolean {
        val query = "SELECT name FROM sqlite_master WHERE type='table' AND name=?"
        val cursor = db.rawQuery(query, arrayOf(tableName))
        val tableExists = cursor.count > 0
        cursor.close()
        return tableExists
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        if (!checkDatabase()) {
            Toast.makeText(context, "Создаём пустую базу...", Toast.LENGTH_SHORT).show()
            super.getWritableDatabase().use { db ->
                onCreate(db)
            }
            val file = File(context.filesDir, "dump.csv")
            file.delete()
        }
        return super.getReadableDatabase()
    }

    private fun checkDatabase(): Boolean {
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        return dbFile.exists()
    }

    private fun isUserExists(username: String, db: SQLiteDatabase): Boolean {
        val query = "SELECT * FROM ${UserContract.TABLE_NAME} WHERE ${UserContract.COLUMN_USERNAME} = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun insertUser(username: String, password: String, db: SQLiteDatabase) {

        deleteUser(DEFAULT_USERNAME, db)


        val values = ContentValues().apply {
            put(UserContract.COLUMN_USERNAME, username)
            put(UserContract.COLUMN_PASSWORD, password)
        }
        db.insert(UserContract.TABLE_NAME, null, values)
    }

    private fun deleteUser(username: String, db: SQLiteDatabase) {
        db.beginTransaction()
        try {
            val whereClause = "${UserContract.COLUMN_USERNAME} = ?"
            val whereArgs = arrayOf(username)
            db.delete(UserContract.TABLE_NAME, whereClause, whereArgs)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }


}