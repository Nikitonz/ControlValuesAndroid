package com.nikitonz.controlvalues

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        dbHelper = DatabaseHelper(this)

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            // Сохранение пользователя в базу данных
            saveUser(username, password)
        }
    }

    private fun saveUser(username: String, password: String) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(UserContract.COLUMN_USERNAME, username)
            put(UserContract.COLUMN_PASSWORD, password)
        }

        val newRowId = db.insert(UserContract.TABLE_NAME, null, values)

        if (newRowId != -1L) {
            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to register user", Toast.LENGTH_SHORT).show()
        }

        db.close()

    }
}