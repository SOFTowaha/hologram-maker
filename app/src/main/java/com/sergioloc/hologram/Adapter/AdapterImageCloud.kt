package com.sergioloc.hologram.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sergioloc.hologram.Dialogs.DialogImageDetail
import com.sergioloc.hologram.Interfaces.GalleryInterface
import com.sergioloc.hologram.Models.FirebaseImage
import com.sergioloc.hologram.R
import java.util.ArrayList

class AdapterImageCloud(var imageList: ArrayList<String>, var user: FirebaseUser, var mStorage: StorageReference,
                        var context: Context, var interactor: GalleryInterface.Interactor):
        RecyclerView.Adapter<AdapterImageCloud.MyViewHolder>() {

    private var firebaseImageList: ArrayList<FirebaseImage>? = null
    private var dialog: DialogImageDetail? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_image, parent, false)
        firebaseImageList = ArrayList()
        dialog = DialogImageDetail(context, true, interactor)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int { return imageList.size }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var path = "gs://hologram-2.appspot.com/images/users/" + user.uid + "/" + imageList[position]
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl(path)
        mStorage.downloadUrl.addOnSuccessListener { uri ->
            firebaseImageList?.add(FirebaseImage(position, uri))
            Log.i("IMG", "Posicion -> " + position + " | Imagen: " + imageList[position])

            Glide.with(context)
                    .load(uri)
                    .into(holder.image)
        }

        holder.image.setOnClickListener {
            Log.i("IMG", "Buscando -> $position")
            for (i in firebaseImageList!!) {
                if (position == i.position)
                    dialog?.startDialogUri(i.image, imageList[position], position)
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image = itemView.findViewById(R.id.ivImage) as ImageView
    }

}