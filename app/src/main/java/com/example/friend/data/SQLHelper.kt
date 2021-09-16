package com.example.friend.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.friend.model.Friend


internal const val FRIEND_NAME = "FRIEND_NAME"
internal const val FRIEND_AGE = "FRIEND_AGE"
internal const val FRIEND_WEIGHT = "FRIEND_WEIGHT"

private const val LOG_TAG = "SQLHelper"
private const val DATABASE_NAME = "FRIEND_DATABASE"
private const val TABLE_NAME = "friends_table"
private const val DATABASE_VERSION = 1
private const val CREATE_TABLE_SQL =
    "CREATE TABLE IF NOT EXISTS $TABLE_NAME " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$FRIEND_NAME VARCHAR(50), " +
            "$FRIEND_AGE VARCHAR(50), " +
            "$FRIEND_WEIGHT VARCHAR(50));"

class SQLHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL(CREATE_TABLE_SQL)

        } catch (exception: SQLException) {
            Log.e(LOG_TAG, "Exception while trying to create database", exception)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(LOG_TAG, "onUpgrade called")
    }

    fun getCursorWithTopics(): Cursor {
        return readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun getListOfFriends(): List<Friend> {
        val listOfFriends = mutableListOf<Friend>()
        getCursorWithTopics().use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val friendName = cursor.getString(cursor.getColumnIndex(FRIEND_NAME).toInt())
                    val friendAge = cursor.getString(cursor.getColumnIndex(FRIEND_AGE).toInt())
                    val friendWeight = cursor.getString(cursor.getColumnIndex(FRIEND_WEIGHT).toInt())
                    listOfFriends.add(Friend(friendName, friendAge, friendWeight))
                } while (cursor.moveToNext())
            }
        }
        return listOfFriends
    }

    fun insert(friend: Friend) {

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(FRIEND_NAME, friend.name)
        cv.put(FRIEND_AGE, friend.age)
        cv.put(FRIEND_WEIGHT, friend.weight)

        db.insert(TABLE_NAME, null, cv)

        db.close()

    }

    fun update(friend: Friend) {
        delete(friend)
        insert(friend)
    }

    fun delete(friend: Friend) {
        val db = this.writableDatabase

        db.delete(
            TABLE_NAME, "$FRIEND_NAME = ? AND $FRIEND_AGE = ? AND $FRIEND_WEIGHT = ?",
            arrayOf(friend.name, friend.age, friend.weight)
        )
        db.close()
    }
}
