package com.example.Myguelprojetnativesolo.presenter

import com.example.Myguelprojetnativesolo.contract.NoteContract
import com.example.Myguelprojetnativesolo.model.Category
import com.example.Myguelprojetnativesolo.model.Note
import com.example.Myguelprojetnativesolo.model.NoteRepository

/** Gère la logique de la liste des notes */
// Code généré à l'aide de Claude (Anthropic)
// Presenters gérant la logique métier de la liste et de l'éditeur de notes
class NoteListPresenter(private val view: NoteContract.ListView) : NoteContract.ListPresenter {

    private val repository = NoteRepository

    override fun loadNotes() {
        val notes = repository.getAllNotes()
        if (notes.isEmpty()) view.showEmpty() else view.showNotes(notes)
    }

    override fun searchNotes(keyword: String) {
        if (keyword.isBlank()) { loadNotes(); return }
        val results = repository.search(keyword)
        if (results.isEmpty()) view.showEmpty() else view.showNotes(results)
    }

    override fun sortByTitle() =
        view.showNotes(repository.getAllNotes().sortedBy { it.title })

    override fun sortByDateCreation() =
        view.showNotes(repository.getAllNotes().sortedByDescending { it.dateCreation })

    override fun sortByDateModification() =
        view.showNotes(repository.getAllNotes().sortedByDescending { it.dateModification })

    override fun onNoteClicked(note: Note) = view.navigateToEditor(note)
    override fun onFabClicked() = view.navigateToNewNote()
    override fun onNoteLongPressed(note: Note) = view.showDeleteConfirmation(note)
    override fun deleteNote(id: String) { repository.deleteNote(id); loadNotes() }
}

/** Gère la logique de création et d'édition d'une note */
class NoteEditorPresenter(private val view: NoteContract.EditorView) : NoteContract.EditorPresenter {

    private val repository = NoteRepository
    private var originalNote: Note? = null
    private var editMode = false

    override fun loadNote(note: Note?) {
        if (note != null) { originalNote = note; editMode = true; view.showNote(note) }
        else editMode = false
    }

    override fun saveNote(title: String, content: String, category: Category) {
        if (editMode && originalNote != null)
            repository.updateNote(originalNote!!.copy(title = title, content = content, category = category))
        else
            repository.addNote(Note(title = title, content = content, category = category))
        view.showSaveSuccess()
        view.navigateBack()
    }

    override fun deleteNote() {
        originalNote?.let { repository.deleteNote(it.id); view.showDeleteSuccess(); view.navigateBack() }
    }

    override fun isEditMode() = editMode

    /** Vérifie les changements avant de quitter */
    override fun onBackPressed(currentTitle: String, currentContent: String) {
        val original = originalNote
        val hasChanges = if (editMode && original != null)
            currentTitle != original.title || currentContent != original.content
        else currentTitle.isNotBlank() || currentContent.isNotBlank()
        if (hasChanges) view.showUnsavedChangesDialog() else view.navigateBack()
    }
}