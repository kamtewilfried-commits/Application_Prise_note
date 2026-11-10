package com.example.Myguelprojetnativesolo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.Myguelprojetnativesolo.model.NoteRepository
import com.example.Myguelprojetnativesolo.view.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteEndToEndTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        val field = NoteRepository::class.java.getDeclaredField("notes")
        field.isAccessible = true
        (field.get(NoteRepository) as MutableList<*>).clear()
    }

    @Test
    fun parcours_complet_creer_modifier_supprimer() {
        // Aller à la liste des notes
        onView(withId(R.id.button)).perform(click())

        // Créer une note
        onView(withId(R.id.addTodoButton)).perform(click())
        onView(withId(R.id.et_note_title)).perform(typeText("Ma note"), closeSoftKeyboard())
        onView(withId(R.id.et_note_content)).perform(typeText("Contenu initial"), closeSoftKeyboard())
        onView(withId(R.id.btn_save)).perform(click())

        // Vérifier que la note apparaît
        onView(withText("Ma note")).check(matches(isDisplayed()))

        // Modifier la note
        onView(withText("Ma note")).perform(click())
        onView(withId(R.id.et_note_title)).perform(clearText(), typeText("Titre modifié"), closeSoftKeyboard())
        onView(withId(R.id.btn_save)).perform(click())

        // Vérifier le nouveau titre
        onView(withText("Titre modifié")).check(matches(isDisplayed()))

        // Supprimer la note
        onView(withText("Titre modifié")).perform(longClick())
        onView(withText("Supprimer")).perform(click())

        // Vérifier que la note n'existe plus
        onView(withText("Titre modifié")).check(doesNotExist())
        onView(withId(R.id.tv_empty)).check(matches(isDisplayed()))
    }

    @Test
    fun parcours_recherche_plusieurs_notes() {
        // Aller à la liste des notes
        onView(withId(R.id.button)).perform(click())

        // Créer note 1
        onView(withId(R.id.addTodoButton)).perform(click())
        onView(withId(R.id.et_note_title)).perform(typeText("Apprendre Kotlin"), closeSoftKeyboard())
        onView(withId(R.id.et_note_content)).perform(typeText("Coroutines"), closeSoftKeyboard())
        onView(withId(R.id.btn_save)).perform(click())

        // Créer note 2
        onView(withId(R.id.addTodoButton)).perform(click())
        onView(withId(R.id.et_note_title)).perform(typeText("Recette gâteau"), closeSoftKeyboard())
        onView(withId(R.id.et_note_content)).perform(typeText("Farine, oeufs"), closeSoftKeyboard())
        onView(withId(R.id.btn_save)).perform(click())

        // Vérifier que les 2 notes sont visibles
        onView(withText("Apprendre Kotlin")).check(matches(isDisplayed()))
        onView(withText("Recette gâteau")).check(matches(isDisplayed()))

        // Rechercher "Kotlin"
        onView(withId(R.id.search_bar_list_todos)).perform(typeText("Kotlin"), closeSoftKeyboard())

        // Vérifier que seule la note Kotlin s'affiche
        onView(withText("Apprendre Kotlin")).check(matches(isDisplayed()))
        onView(withText("Recette gâteau")).check(doesNotExist())

        // Effacer la recherche → les 2 notes réapparaissent
        onView(withId(R.id.search_bar_list_todos)).perform(clearText())
        onView(withText("Recette gâteau")).check(matches(isDisplayed()))
    }
}