package com.nikitonz.controlvalues

import android.provider.BaseColumns

object UserContract {
    const val TABLE_NAME = "users"
    const val COLUMN_USERNAME = "username"
    const val COLUMN_PASSWORD = "password"

    const val SQL_CREATE_TABLE = """
        CREATE TABLE $TABLE_NAME (
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            $COLUMN_USERNAME TEXT,
            $COLUMN_PASSWORD TEXT
        )
    """

    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}