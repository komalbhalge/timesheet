package com.bnm.timesheet.export

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.util.*

internal class DBQueries(private val context: Context) {
    public final var database: SQLiteDatabase? = null
    private var dbHelper: DBHelper? = null
    @Throws(SQLException::class)
    fun open(): DBQueries {
        dbHelper = DBHelper(context)
        database = dbHelper!!.writableDatabase
        return this
    }

    fun close() {
        dbHelper!!.close()
    }

    // Users
    fun insertUser(users: Users): Boolean {
        val values = ContentValues()
        values.put(DBConstants.CONTACT_PERSON_NAME, users.contactPersonName)
        values.put(DBConstants.CONTACT_NO, users.contactNumber)
        values.put(DBConstants.CONTACT_PHOTO, users.contactPhoto)
        return database!!.insert(DBConstants.USER_TABLE, null, values) > -1
    }

    fun readUsers(): ArrayList<Users> {
        val list = ArrayList<Users>()
        try {
            val cursor: Cursor
            database = dbHelper!!.readableDatabase
            var mdatabase = database
            cursor = (mdatabase?.rawQuery(DBConstants.SELECT_QUERY, null) ?: list.clear()) as Cursor
            if (cursor.count > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        val contactId = cursor.getString(cursor.getColumnIndex(DBConstants.CONTACT_ID))
                        val conPerson = cursor.getString(cursor.getColumnIndex(DBConstants.CONTACT_PERSON_NAME))
                        val conNo = cursor.getString(cursor.getColumnIndex(DBConstants.CONTACT_NO))
                        val conPhoto = cursor.getBlob(cursor.getColumnIndex(DBConstants.CONTACT_NO))
                        val users = Users(contactId, conPerson, conNo, conPhoto)
                        list.add(users)
                    } while (cursor.moveToNext())
                }
            }
            cursor.close()
        } catch (e: Exception) {
            Log.v("Exception", e.message)
        }
        return list
    }
}