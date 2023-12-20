package com.nikitonz.controlvalues

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat.startActivity

class RegistrationActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var dbHelper = DatabaseHelper(this)
        dbHelper.close()


        setContentView(R.layout.activity_registration)
        val editTextUsername = findViewById<EditText>(R.id.sign_in_email)
        val editTextPassword = findViewById<EditText>(R.id.sign_in_password)
        val buttonRegister = findViewById<AppCompatButton>(R.id.buttonRegister)
        val goTouchGrass = findViewById<TextView>(R.id.orSnow)


        buttonRegister.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            saveUser(username, password)

        }
        goTouchGrass.setOnClickListener {

            finish()
        }
    }
    private fun saveUser(username: String, password: String) {
        try {
            val dbHelper = DatabaseHelper(this)
            val db = dbHelper.writableDatabase
            val selection = "${UserContract.COLUMN_USERNAME} = ?"
            val selectionArgs = arrayOf(username)

            val cursor = db.query(
                UserContract.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
            )

            val userExists = cursor.count > 0
            cursor.close()

            if (userExists) {
                Toast.makeText(this, "Пользователь с таким именем уже существует", Toast.LENGTH_SHORT).show()
            } else {
                if (username.isBlank() || password.isBlank()) {
                    Toast.makeText(this, "Логин и пароль не могут быть пустыми", Toast.LENGTH_SHORT).show()
                } else {
                    if (password.length < 4 || username.length < 3) {
                        Toast.makeText(this, "Требования: логин > 3, пароль > 4", Toast.LENGTH_SHORT).show()
                    } else {
                        dbHelper.insertUser(username, password, db)
                        Toast.makeText(this, "Новый пользователь зарегистрирован", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Registration", "Exception: ${e.message}", e)
        }
    }

}