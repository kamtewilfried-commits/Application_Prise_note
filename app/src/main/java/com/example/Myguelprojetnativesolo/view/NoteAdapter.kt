package com.example.Myguelprojetnativesolo.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Myguelprojetnativesolo.model.Category
import com.example.Myguelprojetnativesolo.model.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**affiche chaque note via item_note.xml */
// Code généré à l'aide de Claude
// Activité recherche, tri
class NoteAdapter(
    private var notes: List<Note>,
    private val onNoteClick: (Note) -> Unit,
    private val onNoteLongClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    fun updateNotes(newNotes: List<Note>) { notes = newNotes; notifyDataSetChanged() }

    fun getNoteAt(position: Int): Note = notes[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false))

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.tvTitle.text = note.title
        holder.tvPreview.text = if (note.content.length > 80) note.content.substring(0, 80) + "..." else note.content
        holder.tvCategory.text = note.category.name
        holder.tvDate.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(note.dateModification))
        holder.categoryIndicator.setBackgroundColor(when (note.category) {
            Category.WORK      -> Color.parseColor("#2196F3")
            Category.IDEA      -> Color.parseColor("#FFC107")
            Category.IMPORTANT -> Color.parseColor("#F44336")
            Category.GENERAL   -> Color.parseColor("#9E9E9E")
        })
        holder.itemView.setOnClickListener { onNoteClick(note) }
        holder.itemView.setOnLongClickListener { onNoteLongClick(note); true }
    }

    override fun getItemCount() = notes.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView       = itemView.findViewById(R.id.tv_note_title)
        val tvPreview: TextView     = itemView.findViewById(R.id.tv_note_preview)
        val tvDate: TextView        = itemView.findViewById(R.id.tv_note_date)
        val tvCategory: TextView    = itemView.findViewById(R.id.tv_note_category)
        val categoryIndicator: View = itemView.findViewById(R.id.category_indicator)
    }


}