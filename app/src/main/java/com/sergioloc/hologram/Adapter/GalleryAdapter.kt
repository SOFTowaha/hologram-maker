package com.sergioloc.hologram.adapter

import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.sergioloc.hologram.dialogs.DialogImageDetail
import com.sergioloc.hologram.R
import java.util.ArrayList

class GalleryAdapter(var images: ArrayList<Bitmap>): RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {

    private var dialog: DialogImageDetail? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_image, parent, false)
        dialog = DialogImageDetail(view.context)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (images.isNotEmpty()) {
            holder.image.setImageBitmap(images[position])
        }

        holder.image.setOnClickListener {
            dialog?.startDialogBitmap(images[position], "", position)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image = itemView.findViewById(R.id.ivImage) as ImageView
    }

}