package com.example.Myguelprojetnativesolo.contract

import com.example.Myguelprojetnativesolo.model.Category
import com.example.Myguelprojetnativesolo.model.Note

interface NoteContract {

    interface ListView {
        fun showNotes(notes: List<Note>)
        fun showEmpty()
        fun navigateToEditor(note: Note)
        fun navigateToNewNote()
        fun showDeleteConfirmation(note: Note)
    }

    interface ListPresenter {
        fun loadNotes()
        fun searchNotes(keyword: String)
        fun sortByTitle()
        fun sortByDateCreation()
        fun sortByDateModification()
        fun onNoteClicked(note: Note)
        fun onFabClicked()
        fun onNoteLongPressed(note: Note)
        fun deleteNote(id: String)
    }

    interface EditorView {
        fun showNote(note: Note)
        fun showSaveSuccess()
        fun showDeleteSuccess()
        fun showUnsavedChangesDialog()
        fun navigateBack()
    }

    interface EditorPresenter {
        fun loadNote(note: Note?)
        fun saveNote(title: String, content: String, category: Category)
        fun deleteNote()
        fun isEditMode(): Boolean
        fun onBackPressed(currentTitle: String, currentContent: String)
    }
}