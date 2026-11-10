package com.example.Myguelprojetnativesolo.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Myguelprojetnativesolo.R
import com.example.Myguelprojetnativesolo.constants.NoteConstants
import com.example.Myguelprojetnativesolo.contract.NoteContract
import com.example.Myguelprojetnativesolo.model.Note
import com.example.Myguelprojetnativesolo.presenter.NoteListPresenter
// Code généré à l'aide de Claude (Anthropic)
// Activité de création et d'édition d'une note
class NoteListActivity : AppCompatActivity(), NoteContract.ListView {

    private lateinit var presenter: NoteListPresenter
    private lateinit var adapter: NoteAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var emptyText: TextView
    private lateinit var fab: Button

    companion object {
        private const val KEY_SEARCH = "key_search"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewNotes)
        searchBar    = findViewById(R.id.search_bar_list_todos)
        emptyText    = findViewById(R.id.tv_empty)
        fab          = findViewById(R.id.addTodoButton)

        presenter = NoteListPresenter(this)
        adapter   = NoteAdapter(
            emptyList(),
            onNoteClick     = { presenter.onNoteClicked(it) },
            onNoteLongClick = { presenter.onNoteLongPressed(it) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Swipe gauche ou droite → option de suppression
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = adapter.getNoteAt(viewHolder.adapterPosition)
                presenter.onNoteLongPressed(note)
                adapter.notifyItemChanged(viewHolder.adapterPosition)
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView)

        fab.setOnClickListener { presenter.onFabClicked() }

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { presenter.searchNotes(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Restaure le texte de recherche après rotation
        savedInstanceState?.getString(KEY_SEARCH)?.let { keyword ->
            searchBar.setText(keyword)
            presenter.searchNotes(keyword)
        } ?: presenter.loadNotes()
    }

    // Sauvegarde le texte de recherche avant rotation
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH, searchBar.text.toString())
    }

    override fun onResume() { super.onResume(); presenter.loadNotes() }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_sort, menu); return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.sort_title             -> { presenter.sortByTitle(); true }
        R.id.sort_date_creation     -> { presenter.sortByDateCreation(); true }
        R.id.sort_date_modification -> { presenter.sortByDateModification(); true }
        else -> super.onOptionsItemSelected(item)
    }

    override fun showNotes(notes: List<Note>) {
        emptyText.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        adapter.updateNotes(notes)
    }

    override fun showEmpty() {
        recyclerView.visibility = View.GONE
        emptyText.visibility = View.VISIBLE
    }

    override fun navigateToEditor(note: Note) {
        startActivity(Intent(this, NoteEditorActivity::class.java).apply {
            putExtra(NoteConstants.EXTRA_NOTE_ID, note.id)
            putExtra(NoteConstants.EXTRA_IS_EDIT_MODE, true)
        })
    }

    override fun navigateToNewNote() {
        startActivity(Intent(this, NoteEditorActivity::class.java).apply {
            putExtra(NoteConstants.EXTRA_IS_EDIT_MODE, false)
        })
    }

    override fun showDeleteConfirmation(note: Note) {
        AlertDialog.Builder(this)
            .setTitle("Supprimer")
            .setMessage("Supprimer \"${note.title}\" ?")
            .setPositiveButton("Supprimer") { _, _ -> presenter.deleteNote(note.id) }
            .setNegativeButton("Annuler", null)
            .show()
    }
}