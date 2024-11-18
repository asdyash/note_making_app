package com.yash.androidproject

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class AddEditNoteActivity : AppCompatActivity() {
    private lateinit var contentEditText: EditText
    private lateinit var titleEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var colorSpinner: Spinner
    private lateinit var databaseHelper: DatabaseHelper
    private var note: Note? = null
    private var selectedColor: Int = Color.WHITE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)
        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.screencolor)
        contentEditText = findViewById(R.id.contentEditText)
        titleEditText = findViewById(R.id.titleEditText)
        saveButton = findViewById(R.id.saveButton)
        colorSpinner = findViewById(R.id.colorSpinner)
        databaseHelper = DatabaseHelper(this)
        note = intent.getParcelableExtra("note")
        note?.let {
            titleEditText.setText(it.title)
            contentEditText.setText(it.content)
            selectedColor = it.color
        }
        setupColorSpinner()
        saveButton.setOnClickListener {
            saveNote()
        }
    }
    private fun setupColorSpinner() {
        val colors = arrayOf("Purple", "Red", "Green", "Blue", "Yellow")
        val colorValues = arrayOf(R.color.purple, R.color.red, R.color.green, R.color.blue, R.color.yellow)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colors)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        colorSpinner.adapter = adapter
        colorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedColor = ContextCompat.getColor(this@AddEditNoteActivity, colorValues[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        note?.let {
            val noteColor = it.color
            val index = colorValues.indexOfFirst { colorResource ->
                ContextCompat.getColor(this, colorResource) == noteColor
            }
            if (index >= 0) {
                colorSpinner.setSelection(index)
            }
        }
    }
    private fun saveNote() {
        val title = titleEditText.text.toString()
        val content = contentEditText.text.toString()
        if (note == null) {
            val newNote = Note(
                title = title,
                content = content,
                color = selectedColor
            )
            databaseHelper.addNote(newNote)
        } else {
            val updatedNote = note!!.copy(
                title = title,
                content = content,
                color = selectedColor
            )
            databaseHelper.updateNote(updatedNote)
        }
        finish()
    }
}
