package com.sergioloc.hologram.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.sergioloc.hologram.R
import java.util.ArrayList

class GalleryAdapter(var bitmaps: ArrayList<Bitmap>, var listener: OnHologramClickListener): RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_image, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bitmap = bitmaps[position]

        holder.ivImage.setImageBitmap(bitmap)

        holder.ivImage.setOnClickListener {
            listener.onHologramClick(bitmap, position)
        }
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
        var ivImage: ImageView = itemView.findViewById(R.id.ivImage)
    }

    interface OnHologramClickListener {
        fun onHologramClick(bitmap: Bitmap, position: Int)
    }

}