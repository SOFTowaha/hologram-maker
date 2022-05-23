package com.sergioloc.hologram.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sergioloc.hologram.R
import com.sergioloc.hologram.models.News

class NewsAdapter(private val news: ArrayList<News>, private val listener: OnNewsClickListener): RecyclerView.Adapter<NewsAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int = news.size

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val n = news[position]

        // Variables
        holder.tvName.text = n.name
        holder.tvTag.text = n.tag

        // Buttons
        holder.root.setOnClickListener {
            listener.onClickNews("")
        }
    }

    class UserViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.cell_news, parent, false)) {

        var root: ConstraintLayout = itemView.findViewById(R.id.card)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvTag: TextView = itemView.findViewById(R.id.tvTag)
        var ivImage: ImageView = itemView.findViewById(R.id.ivImage)

    }

    interface OnNewsClickListener {
        fun onClickNews(url: String)
    }

}