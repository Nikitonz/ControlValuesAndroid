package com.nikitonz.controlvalues


import android.content.ContentValues
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast

import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat

class LogonActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbHelper = DatabaseHelper(this)
        dbHelper.close()
        setLoginWindow()

    }
    fun gotoMain(){
        val intent = Intent(this, TableViewActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun setLoginWindow(){


        setContentView(R.layout.activity_auth)

        val editTextUsernameLogin = findViewById<EditText>(R.id.sign_in_email)
        val editTextPasswordLogin = findViewById<EditText>(R.id.sign_in_password)
        val buttonLogin = findViewById<AppCompatButton>(R.id.buttonRegister)


        buttonLogin.setOnClickListener {
            val username = editTextUsernameLogin.text.toString()
            val password = editTextPasswordLogin.text.toString()


            if (isUserAuthenticated(username, password)) {
                gotoMain()
            } else {
                Toast.makeText(this, "Ошибка входа", Toast.LENGTH_SHORT).show()
            }
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





    fun isUserAuthenticated(username: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val selection = "${UserContract.COLUMN_USERNAME} = ? AND ${UserContract.COLUMN_PASSWORD} = ?"
        val selectionArgs = arrayOf(username, password)
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
            result = true
        }

        cursor.close()
        db.close()

        return result
    }




}