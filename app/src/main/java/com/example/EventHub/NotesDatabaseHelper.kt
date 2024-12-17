package com.example.EventHub

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
class NotesDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_NAME = "allnotes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("NotesDatabaseHelper", "onCreate called")
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_PRICE REAL)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_PRICE, note.price)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllNotes(): List<Note> {
        val notesList = mutableListOf<Note>()
        val db = readableDatabase
        if (db == null) {
            Log.e("NotesDatabaseHelper", "Database is null")
            return notesList
        }

        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                    val title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE))
                    val content = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTENT))
                    val price = it.getDouble(it.getColumnIndexOrThrow(COLUMN_PRICE))
                    notesList.add(Note(id, title, content, price))
                } while (it.moveToNext())
            }
        }
        return notesList
    }

    fun  deletNote(notId: Int){
       val db = writableDatabase
       val whereClause ="$COLUMN_ID= ?"
       val whereArgs = arrayOf(notId.toString())
       db.delete(TABLE_NAME,whereClause,whereArgs)
       close()
    }
}
