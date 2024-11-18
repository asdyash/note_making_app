package com.yash.androidproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var emptyMessageLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.screencolor)
        databaseHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        notesAdapter = NotesAdapter(this, ::onEditNote, ::onDeleteNote)
        recyclerView.adapter = notesAdapter
        emptyMessageLayout = findViewById(R.id.emptyMessageLayout)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            startActivity(intent)
        }
        loadNotes()
    }
    override fun onResume() {
        super.onResume()
        loadNotes()
    }
    private fun loadNotes() {
        val notes = databaseHelper.getAllNotes()
        notesAdapter.setNotes(notes)
        if (notes.isEmpty()) {
            emptyMessageLayout.visibility = View.VISIBLE
        } else {
            emptyMessageLayout.visibility = View.GONE
        }
    }
    private fun onEditNote(note: Note) {
        val intent = Intent(this, AddEditNoteActivity::class.java).apply {
            putExtra("note", note)
        }
        startActivity(intent)
    }
    private fun onDeleteNote(note: Note) {
        databaseHelper.deleteNote(note)
        loadNotes()
    }
}
