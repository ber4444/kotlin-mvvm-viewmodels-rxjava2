package com.test

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lib.model.Event
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class SecondAdapter : RecyclerView.Adapter<SecondAdapter.ViewHolder>() {

    private val recyclerList = mutableListOf<Event>()
    private var listener: RecyclerViewClickListener? = null

    fun add(item: Event) {
        recyclerList.add(item)
        this.notifyItemInserted(recyclerList.count() - 1)
    }

    override fun onBindViewHolder(viewHolder: SecondAdapter.ViewHolder, position: Int) {
        viewHolder.bind(recyclerList[position])
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): SecondAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_second, viewGroup, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return recyclerList.count()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        private val textView : TextView = itemView.findViewById(R.id.item_text)
        private val dateView : TextView = itemView.findViewById(R.id.item_date)
        private val imageView : ImageView = itemView.findViewById(R.id.item_image)
        private var listener: RecyclerViewClickListener? = null

        constructor(v: View, l: RecyclerViewClickListener?): this(v) {
            listener = l
            v.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(i: Event) {
            Picasso.get().load(i.from.avatarUrl).resize(200, 300).centerInside().into(imageView)
            if (i.message != null) textView.text = i.message!!.text.subSequence(0, Math.min(i.message!!.text.length, 140)) // good old twitter limit
            else if (i.comment != null) textView.text = i.comment!!.text.subSequence(0, Math.min(i.comment!!.text.length, 140))
            dateView.text = toSimpleString(Date(i.date))
        }

        override fun onClick(view: View?) {
            if (view != null) listener?.onClick(view, adapterPosition)

        }

        private fun toSimpleString(date: Date?) = with(date ?: Date()) {
            SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US).format(this)
        }
    }
}
