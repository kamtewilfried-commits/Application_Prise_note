// Code généré à l'aide de Claude
// fonction search et removeIf
package com.example.Myguelprojetnativesolo.model

object NoteRepository {

    private val notes = mutableListOf<Note>()

    fun getAllNotes(): List<Note> = notes.toList()

    fun addNote(note: Note) { notes.add(note) }

    fun updateNote(updated: Note) {
        val index = notes.indexOfFirst { it.id == updated.id }
        if (index != -1)
            notes[index] = updated.copy(dateModification = System.currentTimeMillis())
    }

    // Remplacement de removeIf (non supporté avant API 24)
    fun deleteNote(id: String) {
        val index = notes.indexOfFirst { it.id == id }
        if (index != -1) notes.removeAt(index)
    }

    fun search(keyword: String): List<Note> {
        val kw = keyword.lowercase()
        return notes.filter {
            it.title.lowercase().contains(kw) ||
                    it.content.lowercase().contains(kw)
        }
    }

    fun getNoteById(id: String): Note? = notes.find { it.id == id }
}