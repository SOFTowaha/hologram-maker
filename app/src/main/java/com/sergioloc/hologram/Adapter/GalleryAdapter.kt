package com.sergioloc.hologram.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.sergioloc.hologram.dialogs.DialogImageDetail
import com.sergioloc.hologram.R
import java.util.ArrayList

class GalleryAdapter(var bitmaps: ArrayList<Bitmap>): RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {

    private var dialog: DialogImageDetail? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_image, parent, false)
        dialog = DialogImageDetail(view.context)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bitmap = bitmaps[position]

        holder.ivImage.setImageBitmap(bitmap)

        holder.ivImage.setOnClickListener {
            dialog?.startDialogBitmap(bitmap, "", position)
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

    fun addItem(bitmap: Bitmap) {
        bitmaps.add(bitmap)
        notifyItemInserted(bitmaps.size-1)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivImage: ImageView = itemView.findViewById(R.id.ivImage)
    }

}