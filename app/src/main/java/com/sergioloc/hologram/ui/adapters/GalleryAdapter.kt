package com.sergioloc.hologram.ui.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.sergioloc.hologram.R
import com.sergioloc.hologram.domain.model.Gallery
import com.sergioloc.hologram.utils.Converter

class GalleryAdapter(var bitmaps: List<Gallery>, var listener: OnHologramClickListener): RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {

    private var margin = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_image, parent, false)
        margin = Converter.dpToPx(parent.context, 20f)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val image = bitmaps[position]

        holder.ivImage.setImageBitmap(image.bitmap)

        holder.ivImage.setOnClickListener {
            listener.onHologramClick(image)
        }
    }

    override fun getItemCount(): Int {
        return bitmaps.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<Gallery>) {
        bitmaps = list
        notifyDataSetChanged()
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var root: ConstraintLayout = itemView.findViewById(R.id.root)
        var ivImage: ImageView = itemView.findViewById(R.id.ivImage)
    }

    interface OnHologramClickListener {
        fun onHologramClick(image: Gallery)
    }

}