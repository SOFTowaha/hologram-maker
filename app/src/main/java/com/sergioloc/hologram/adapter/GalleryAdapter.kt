package com.sergioloc.hologram.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.sergioloc.hologram.R
import com.sergioloc.hologram.utils.Converter
import java.util.ArrayList

class GalleryAdapter(var bitmaps: ArrayList<Bitmap>, var listener: OnHologramClickListener): RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {

    private var margin = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_image, parent, false)
        margin = Converter.dpToPx(parent.context, 20f)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bitmap = bitmaps[position]
        //setMargins(holder.root, position)

        holder.ivImage.setImageBitmap(bitmap)

        holder.ivImage.setOnClickListener {
            listener.onHologramClick(bitmap, position)
        }
    }

    private fun setMargins(root: ConstraintLayout, position: Int) {
        val rootParams = root.layoutParams as ViewGroup.MarginLayoutParams
        // Left
        if (position == 0 || position % 3 == 0)
            rootParams.setMargins(margin, 0, margin/2, 0)
        // Right
        else if (position % 2 == 0)
            rootParams.setMargins(margin/2, 0, margin, 0)
        // Left
        else
            rootParams.setMargins(margin/2, 0, margin/2, 0)

        root.layoutParams = rootParams
    }

    override fun getItemCount(): Int {
        return bitmaps.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ArrayList<Bitmap>) {
        bitmaps = list
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(bitmap: Bitmap) {
        bitmaps.add(bitmap)
        notifyDataSetChanged()
        //notifyItemInserted(bitmaps.size-1)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(position: Int) {
        bitmaps.removeAt(position)
        notifyDataSetChanged()
        //notifyItemRemoved(position)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var root: ConstraintLayout = itemView.findViewById(R.id.root)
        var ivImage: ImageView = itemView.findViewById(R.id.ivImage)
    }

    interface OnHologramClickListener {
        fun onHologramClick(bitmap: Bitmap, position: Int)
    }

}