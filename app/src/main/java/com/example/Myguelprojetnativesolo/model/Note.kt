package com.example.Myguelprojetnativesolo.model

// data class = génère automatiquement equals(), hashCode(), copy(), toString()
import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    var title: String,
    var content: String,
    var category: Category,
    val dateCreation: Long = System.currentTimeMillis(),
    var dateModification: Long = System.currentTimeMillis()
)