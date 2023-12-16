package com.nikitonz.controlvalues


import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import android.util.Log
import androidx.lifecycle.viewmodel.CreationExtras

class RegLogonActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    fun setRegWindow(){
        setContentView(R.layout.activity_registration)
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val buttonSwitchToLogin = findViewById<Button>(R.id.buttonSwitchToLogin)

        buttonRegister.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            saveUser(username, password)
        }
        buttonSwitchToLogin.setOnClickListener {
            setLoginWindow()
        }


        val dropDbButton = findViewById<Button>(R.id.drop_db)
        dropDbButton.setOnClickListener {
            val dbHelper = DatabaseHelper(this)

            val db = dbHelper.writableDatabase
            db.execSQL("DROP TABLE IF EXISTS ${UserContract.TABLE_NAME}")

            db.close()
            deleteDatabase(DatabaseHelper.DATABASE_NAME)

            Toast.makeText(this, "Database dropped", Toast.LENGTH_SHORT).show()
        }

    }

    fun setLoginWindow(){
        setContentView(R.layout.activity_auth)
        val buttonSwitchToRegister = findViewById<Button>(R.id.buttonSwitchToRegister)
        val editTextUsernameLogin = findViewById<EditText>(R.id.editTextUsernameLogin)
        val editTextPasswordLogin = findViewById<EditText>(R.id.editTextPasswordLogin)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)


        buttonLogin.setOnClickListener {
            val username = editTextUsernameLogin.text.toString()
            val password = editTextPasswordLogin.text.toString()


            if (isUserAuthenticated(username, password)) {

                gotoMain()
            } else {

                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        }
        buttonSwitchToRegister.setOnClickListener {
            setRegWindow()
        }
    }



    private fun saveUser(username: String, password: String) {


        try {


            if (isUserExists(username)) {
                Toast.makeText(this, "User with this username already exists", Toast.LENGTH_SHORT)
                    .show()
                setLoginWindow()
            } else {
                if (username=="" || password==""){
                    Toast.makeText(this, "login or password cannot be empty",Toast.LENGTH_SHORT).show()
                    throw Exception("login or password cannot be empty")}
                if (password.length<4 || username.length<3){
                    Toast.makeText(this, "login cannot be less than 3; password cannot be less than 4",Toast.LENGTH_SHORT).show()
                    throw Exception("login or password cannot be less than limitations")
                }
                val values = ContentValues().apply {
                    put(UserContract.COLUMN_USERNAME, username)
                    put(UserContract.COLUMN_PASSWORD, password)
                }
                var db = dbHelper.writableDatabase
                val newRowId = db.insert(UserContract.TABLE_NAME, null, values)
                db.close()
                if (newRowId != -1L) {
                    Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to register user", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("Registration", "Exception: ${e.message}", e)
        }
    }

    fun isUserAuthenticated(username: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val selection = "${UserContract.COLUMN_USERNAME} = ?"
        val selectionArgs = arrayOf(username)
        var result: Boolean = false
        val cursor = db.query(
            UserContract.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            val passwordColumnIndex = cursor.getColumnIndex(UserContract.COLUMN_PASSWORD)
            if (passwordColumnIndex != -1)
                result = true


            cursor.close()
            db.close()


        }
        return result
    }
    private fun isUserExists(username: String): Boolean {
        val db = dbHelper.readableDatabase
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
        db.close()

        return userExists
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)
        dbHelper.close()
        setRegWindow()
    }
    fun gotoMain(){
        try {
            val intent = Intent(this, TableViewActivity::class.java)
            startActivity(intent)
        }catch (e:Exception){
            Log.e("tag", e.toString())
        }


    }
}