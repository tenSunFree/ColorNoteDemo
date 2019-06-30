package com.home.colornotedemo.main.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.home.colornotedemo.R
import com.home.colornotedemo.main.model.NoteData

class NotesAdapter : BaseAdapter() {

    private val noteList: MutableList<NoteData> = mutableListOf()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
            ?: LayoutInflater.from(parent.context) // ?:表示 如果convertView不為null，則elvis運算符返回convertView; 否則返回右邊的表達式; 請注意 僅當左側為空時才評估右側表達式
                .inflate(R.layout.fragment_main_home_page_list_view_item, parent, false)
                .also { it.tag = NoteViewHolder(it) } // 保存tag, 以達到重用的目的; also: 將自己(this)回傳或傳入下個chain
        val holder = view.tag as NoteViewHolder
        val note = getNote(position)
        if (note != null) {
            holder.contentTextView.text = note.content
            holder.monthDayTextView.text = note.lastModifiedMonthDay
        } else {
            holder.contentTextView.text = ""
            holder.monthDayTextView.text = ""
        }
        val lastPosition = noteList.size - 1
        if (position == lastPosition) {
            holder.view.visibility = View.GONE
        } else {
            holder.view.visibility = View.VISIBLE
        }
        return view
    }

    fun setNotes(notes: List<NoteData>) {
        noteList.apply {
            clear() // clear removeAll
            addAll(notes) // addAll(): 可以添加另一个List, Array, Set..等, 看起来是序列的
        }
        notifyDataSetChanged()
    }

    private fun getNote(position: Int): NoteData? {
        return if (position >= 0 && position < noteList.size) {
            noteList[position]
        } else {
            null
        }
    }

    override fun getCount(): Int = noteList.size

    override fun getItemId(position: Int): Long = noteList[position].id

    override fun getItem(position: Int) {}

    private class NoteViewHolder(itemView: View) {
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val monthDayTextView: TextView = itemView.findViewById(R.id.monthDayTextView)
        val view: View = itemView.findViewById(R.id.view)
    }
}