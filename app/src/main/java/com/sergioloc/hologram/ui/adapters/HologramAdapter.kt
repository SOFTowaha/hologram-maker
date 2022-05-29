package com.sergioloc.hologram.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.needle.app.utils.extensions.setImageFromURL
import com.needle.app.utils.extensions.setOnSingleClickListener
import com.sergioloc.hologram.R
import com.sergioloc.hologram.data.model.Hologram
import com.sergioloc.hologram.utils.Converter

class HologramAdapter(private var holograms: ArrayList<Hologram>, private val listener: OnNewsClickListener): RecyclerView.Adapter<HologramAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int = holograms.size

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val n = holograms[position]

        // Text
        holder.tvName.text = n.name
        holder.tvTag.text = n.tag
        holder.tvTag.setTextColor(Converter.getTagColor(holder.root.context, n.tag))

        // Image
        setImageFromURL(holder.root.context, n.image) {
            holder.ivImage.setImageDrawable(it)
        }

        // Buttons
        holder.root.setOnSingleClickListener {
            listener.onClickNews("")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ArrayList<Hologram>) {
        holograms = list
        notifyDataSetChanged()
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