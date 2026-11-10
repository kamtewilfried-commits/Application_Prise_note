package com.example.Myguelprojetnativesolo.model

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
// Code généré à l'aide de Claude (Anthropic)
// Tests

class NoteRepositoryTest {

    @Before
    fun setUp() {
        val field = NoteRepository::class.java.getDeclaredField("notes")
        field.isAccessible = true
        (field.get(NoteRepository) as MutableList<*>).clear()
    }

    @Test
    fun `creer une note avec donnees valides`() {
        val note = Note(title = "Ma note", content = "Contenu test", category = Category.GENERAL)
        NoteRepository.addNote(note)
        val notes = NoteRepository.getAllNotes()
        assertEquals(1, notes.size)
        assertEquals("Ma note", notes[0].title)
        assertEquals(Category.GENERAL, notes[0].category)
        assertNotNull(notes[0].id)
    }

    @Test
    fun `recherche par mot cle dans le titre`() {
        NoteRepository.addNote(Note(title = "Recette gâteau", content = "Farine", category = Category.GENERAL))
        NoteRepository.addNote(Note(title = "Meeting travail", content = "Agenda", category = Category.WORK))
        val results = NoteRepository.search("recette")
        assertEquals(1, results.size)
        assertEquals("Recette gâteau", results[0].title)
    }

    @Test
    fun `recherche par mot cle dans le contenu`() {
        NoteRepository.addNote(Note(title = "Note A", content = "Acheter du lait", category = Category.GENERAL))
        NoteRepository.addNote(Note(title = "Note B", content = "Réunion 14h", category = Category.WORK))
        val results = NoteRepository.search("lait")
        assertEquals(1, results.size)
        assertEquals("Note A", results[0].title)
    }

    @Test
    fun `tri par date de modification`() {
        NoteRepository.addNote(Note(title = "Note 1", content = "", category = Category.GENERAL)); Thread.sleep(10)
        NoteRepository.addNote(Note(title = "Note 2", content = "", category = Category.WORK)); Thread.sleep(10)
        NoteRepository.addNote(Note(title = "Note 3", content = "", category = Category.IDEA))
        val sorted = NoteRepository.getAllNotes().sortedByDescending { it.dateModification }
        assertEquals("Note 3", sorted[0].title)
        assertEquals("Note 1", sorted[2].title)
    }

    @Test
    fun `mise a jour de la date de modification`() {
        val note = Note(title = "Titre", content = "Contenu", category = Category.GENERAL)
        NoteRepository.addNote(note)
        val dateAvant = NoteRepository.getAllNotes()[0].dateModification
        Thread.sleep(50)
        NoteRepository.updateNote(note.copy(title = "Nouveau titre"))
        val dateApres = NoteRepository.getAllNotes()[0].dateModification
        assertTrue(dateApres > dateAvant)
    }

    @Test
    fun `supprimer une note`() {
        val note = Note(title = "À supprimer", content = "", category = Category.GENERAL)
        NoteRepository.addNote(note)
        NoteRepository.deleteNote(note.id)
        assertEquals(0, NoteRepository.getAllNotes().size)
    }

    @Test
    fun `recherche insensible a la casse`() {
        NoteRepository.addNote(Note(title = "KOTLIN", content = "", category = Category.WORK))
        assertEquals(1, NoteRepository.search("kotlin").size)
    }
}