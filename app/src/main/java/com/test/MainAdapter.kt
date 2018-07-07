package com.test

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lib.model.Conversation

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val recyclerList = mutableListOf<Conversation>()
    var listener: RecyclerViewClickListener? = null

    fun addAll(items: List<Conversation>) {
        val initialSize = recyclerList.size
        recyclerList.addAll(items)
        val updatedSize = recyclerList.size
        notifyItemRangeInserted(initialSize,updatedSize)
    }

    fun getId(index: Int): Long {
        return recyclerList[index].id
    }

    override fun onBindViewHolder(viewHolder: MainAdapter.ViewHolder, position: Int) {
        viewHolder.bind(recyclerList[position])
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MainAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_main, viewGroup, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return recyclerList.count()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        private val itemTextView : TextView = itemView.findViewById(R.id.recycler_row_text_view)
        private var listener: RecyclerViewClickListener? = null

        constructor(v: View, l: RecyclerViewClickListener?): this(v) {
            listener = l
            v.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(i: Conversation) {
            itemTextView.text = i.from + ": " + i.subject
        }

        override fun onClick(view: View?) {
            if (view != null) listener?.onClick(view, adapterPosition)

        }
    }
}
