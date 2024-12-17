package com.example.EventHub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.EventHub.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NotesDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString().trim()
            val content = binding.ServiceEditText.text.toString().trim()
            val priceString = binding.priceEditText.text.toString().trim()

            // Validate input fields
            if (title.isEmpty() || content.isEmpty() || priceString.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Attempt to parse the price
            val price: Double = try {
                priceString.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val note = Note(0, title, content, price)
            db.insertNote(note)

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show() // Confirmation message
            finish() // End activity
        }
    }
}
