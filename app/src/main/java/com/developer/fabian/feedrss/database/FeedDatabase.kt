package com.developer.fabian.feedrss.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.developer.fabian.feedrss.entities.Item

import java.util.HashMap

class FeedDatabase private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val COLUMN_ID = 0
        private const val COLUMN_TITLE = 1
        private const val COLUMN_DESCRIPTION = 2
        private const val COLUMN_URL = 3

        private val TAG = FeedDatabase::class.java.simpleName
        private const val DATABASE_NAME = "Feed.db"
        private const val DATABASE_VERSION = 1

        private lateinit var singleton: FeedDatabase

        @Synchronized
        fun getInstance(context: Context): FeedDatabase {
            singleton = FeedDatabase(context.applicationContext)
            return singleton
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ScriptDatabase.CREATE_ENTER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${ScriptDatabase.ENTER_TABLE_NAME}")
        onCreate(db)
    }

    fun syncEntries(entries: List<Item>) {
        val entryMap = HashMap<String, Item>()

        for (e in entries)
            entryMap[e.title!!] = e

        Log.i(TAG, "Retrieve items currently stored")

        val cursor = getEntries()

        Log.i(TAG, "${cursor.count} entries were found, computing...")

        var id: Int
        var title: String
        var description: String
        var url: String

        while (cursor.moveToNext()) {
            id = cursor.getInt(COLUMN_ID)
            title = cursor.getString(COLUMN_TITLE)
            description = cursor.getString(COLUMN_DESCRIPTION)
            url = cursor.getString(COLUMN_URL)
            val match = entryMap[title]

            if (match != null) {
                entryMap.remove(title)

                if (match.title != null && match.title != title ||
                        match.description != null && match.description != description ||
                        match.link != null && match.link != url) {

                    updateEntries(id, match.title, match.description, match.link, match.content!!.url)
                }
            }
        }

        cursor.close()

        for (e in entryMap.values) {
            Log.i(TAG, "Inserted: title=${e.title!!}")
            insertEntries(e.title, e.description, e.link, e.content!!.url)
        }

        Log.i(TAG, "Data has been updated")
    }

    fun getEntries(): Cursor {
        return writableDatabase.rawQuery("SELECT * FROM ${ScriptDatabase.ENTER_TABLE_NAME}", null)
    }

    private fun insertEntries(title: String?, description: String?, url: String?, thumb_url: String?) {
        val values = ContentValues()

        values.put(ScriptDatabase.EnterColumns.TITLE, title)
        values.put(ScriptDatabase.EnterColumns.DESCRIPTION, description)
        values.put(ScriptDatabase.EnterColumns.URL, url)
        values.put(ScriptDatabase.EnterColumns.THUMB_URL, thumb_url)

        writableDatabase.insert(ScriptDatabase.ENTER_TABLE_NAME, null, values)
    }

    private fun updateEntries(id: Int, title: String?, description: String?, url: String?, thumb_url: String?) {
        val values = ContentValues()

        values.put(ScriptDatabase.EnterColumns.TITLE, title)
        values.put(ScriptDatabase.EnterColumns.DESCRIPTION, description)
        values.put(ScriptDatabase.EnterColumns.URL, url)
        values.put(ScriptDatabase.EnterColumns.THUMB_URL, thumb_url)

        writableDatabase.update(ScriptDatabase.ENTER_TABLE_NAME, values, "${ScriptDatabase.EnterColumns.ID}=?", arrayOf(id.toString()))
    }
}
