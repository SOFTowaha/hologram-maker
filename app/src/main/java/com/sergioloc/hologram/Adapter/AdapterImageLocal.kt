package com.sergioloc.hologram.Adapter

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.sergioloc.hologram.dialogs.DialogImageDetail
import com.sergioloc.hologram.usecases.gallery.GalleryInterface
import com.sergioloc.hologram.R
import java.util.ArrayList

class AdapterImageLocal(var localList: ArrayList<Bitmap>, var context: Context,
                        var interactor: GalleryInterface.Interactor): RecyclerView.Adapter<AdapterImageLocal.MyViewHolder>() {

    private var dialog: DialogImageDetail? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.card_image, parent, false)
        dialog = DialogImageDetail(context, false, interactor)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (localList.isNotEmpty()) {
            holder.image.setImageBitmap(localList[position])
        }

        holder.image.setOnClickListener {
            dialog?.startDialogBitmap(localList[position], "", position)
        }
    }

    private fun isWriteStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERM", "Permission is granted")
                return true
            } else {
                Log.v("PERM", "Permission is revoked")
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 2)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PERM", "Permission is granted")
            return true
        }
    }

    override fun getItemCount(): Int {
        return localList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image = itemView.findViewById(R.id.ivImage) as ImageView
    }

}