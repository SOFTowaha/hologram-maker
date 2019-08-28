package com.sergioloc.hologram.Adapter

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.sergioloc.hologram.Dialogs.ImageDetailDialog
import com.sergioloc.hologram.R
import java.io.ByteArrayOutputStream
import java.util.ArrayList

class AdapterImageLocal(var localList: ArrayList<Bitmap>, var context: Context): RecyclerView.Adapter<AdapterImageLocal.MyViewHolder>() {

    private var dialog: ImageDetailDialog? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.card_image, parent, false)
        dialog = ImageDetailDialog(context)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (localList.isNotEmpty()) {
            holder.image.setImageBitmap(localList[position])
        }

        holder.image.setOnClickListener {
            dialog?.startDialog(getImageUri(context, localList[position]))
        }
    }

    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        if (isWriteStoragePermissionGranted()) {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Title", null)
            return Uri.parse(path)
        } else
            return null
    }

    private fun isWriteStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERM", "Permission is granted2")
                return true
            } else {
                Log.v("PERM", "Permission is revoked2")
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 2)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PERM", "Permission is granted2")
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