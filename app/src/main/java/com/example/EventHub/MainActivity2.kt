package com.example.EventHub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.EventHub.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var db: NotesDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database and adapter
        db = NotesDatabaseHelper(this)

        // Fetch all notes from the database
        val allNotes = db.getAllNotes()

        // Initialize the NotesAdapter with the notes and database helper
        notesAdapter = NotesAdapter(allNotes, this, db)

        // Set up RecyclerView with layout manager and adapter
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = notesAdapter

        // Button click to add a new note
        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning to this activity
        notesAdapter.refreshData(db.getAllNotes())
    }
}
