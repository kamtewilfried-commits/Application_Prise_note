package com.example.Myguelprojetnativesolo.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.Myguelprojetnativesolo.R
import com.example.Myguelprojetnativesolo.constants.NoteConstants
import com.example.Myguelprojetnativesolo.contract.NoteContract
import com.example.Myguelprojetnativesolo.model.Category
import com.example.Myguelprojetnativesolo.model.Note
import com.example.Myguelprojetnativesolo.model.NoteRepository
import com.example.Myguelprojetnativesolo.presenter.NoteEditorPresenter
import java.text.SimpleDateFormat
import java.util.*

// Code généré à l'aide de Claude (Anthropic)
// Activité de création et d'édition d'une note
class NoteEditorActivity : AppCompatActivity(), NoteContract.EditorView {

    private lateinit var presenter: NoteEditorPresenter
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var tvDate: TextView
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    companion object {
        private const val KEY_NOTE_ID      = "key_note_id"
        private const val KEY_IS_EDIT_MODE = "key_is_edit_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)

        etTitle         = findViewById(R.id.et_note_title)
        etContent       = findViewById(R.id.et_note_content)
        spinnerCategory = findViewById(R.id.spinner_category)
        tvDate          = findViewById(R.id.tv_last_modified)
        btnSave         = findViewById(R.id.btn_save)
        btnDelete       = findViewById(R.id.btn_delete)

        spinnerCategory.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            Category.values().map { it.name }
        )

        presenter = NoteEditorPresenter(this)

        // Récupère l'ID et le mode depuis la rotation ou depuis l'Intent
        val noteId = savedInstanceState?.getString(KEY_NOTE_ID)
            ?: intent.getStringExtra(NoteConstants.EXTRA_NOTE_ID)

        val isEditMode = savedInstanceState?.getBoolean(KEY_IS_EDIT_MODE)
            ?: intent.getBooleanExtra(NoteConstants.EXTRA_IS_EDIT_MODE, false)

        if (isEditMode && noteId != null) {
            val note = NoteRepository.getNoteById(noteId)
            presenter.loadNote(note)
        } else {
            presenter.loadNote(null)
            btnDelete.visibility = View.GONE
        }

        btnSave.setOnClickListener {
            if (etTitle.text.isBlank()) {
                etTitle.error = "Le titre est obligatoire"
                return@setOnClickListener
            }
            presenter.saveNote(
                etTitle.text.toString(),
                etContent.text.toString(),
                Category.valueOf(spinnerCategory.selectedItem.toString())
            )
        }

        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Supprimer")
                .setMessage("Voulez-vous supprimer cette note ?")
                .setPositiveButton("Supprimer") { _, _ -> presenter.deleteNote() }
                .setNegativeButton("Annuler", null)
                .show()
        }

        // Remplacement moderne de onBackPressed déprécié
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                presenter.onBackPressed(
                    etTitle.text.toString(),
                    etContent.text.toString()
                )
            }
        })
    }

    // Sauvegarde l'ID et le mode avant rotation
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_NOTE_ID,
            intent.getStringExtra(NoteConstants.EXTRA_NOTE_ID))
        outState.putBoolean(KEY_IS_EDIT_MODE,
            intent.getBooleanExtra(NoteConstants.EXTRA_IS_EDIT_MODE, false))
    }

    override fun showNote(note: Note) {
        etTitle.setText(note.title)
        etContent.setText(note.content)
        spinnerCategory.setSelection(Category.values().indexOf(note.category))
        tvDate.text = "Modifié le : ${
            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(Date(note.dateModification))
        }"
        btnDelete.visibility = View.VISIBLE
    }

    override fun showSaveSuccess() =
        Toast.makeText(this, "Note sauvegardée !", Toast.LENGTH_SHORT).show()

    override fun showDeleteSuccess() =
        Toast.makeText(this, "Note supprimée !", Toast.LENGTH_SHORT).show()

    override fun navigateBack() = finish()

    override fun showUnsavedChangesDialog() {
        AlertDialog.Builder(this)
            .setTitle("Modifications non sauvegardées")
            .setMessage("Voulez-vous quitter sans sauvegarder ?")
            .setPositiveButton("Quitter") { _, _ -> finish() }
            .setNegativeButton("Annuler", null)
            .show()
    }
}