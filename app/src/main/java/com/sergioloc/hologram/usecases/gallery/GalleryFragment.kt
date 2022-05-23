package com.sergioloc.hologram.usecases.gallery

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.needle.app.utils.extensions.setOnSingleClickListener
import com.sergioloc.hologram.dialogs.DialogImageUpload
import com.sergioloc.hologram.R
import com.sergioloc.hologram.adapter.GalleryAdapter
import com.sergioloc.hologram.databinding.FragmentGalleryBinding
import kotlinx.android.synthetic.main.dialog_image_upload.*

class GalleryFragment: Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var viewModel: GalleryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initVariables()
        initObservers()
        initButtons()

        viewModel.getMyHolograms()
    }

    private fun initView() {
        // Toolbar
        val activity = activity as AppCompatActivity
        activity.title = resources.getString(R.string.myholograms)

        // RecyclerView
        binding.rvImages.setHasFixedSize(true)
        binding.rvImages.layoutManager = GridLayoutManager(context, 3)
        binding.rvImages.adapter = GalleryAdapter(ArrayList())
    }

    private fun initVariables() {
        viewModel = GalleryViewModel()
    }

    private fun initObservers() {
        viewModel.list.observe(this) {
            it.onSuccess { list ->
                binding.rvImages.adapter = GalleryAdapter(list)
            }
        }
    }

    private fun initButtons() {
        binding.btnAdd.setOnSingleClickListener {

        }
    }

    private fun showDialog(bitmap: Bitmap, cloudView: Boolean) {
        val dialog = DialogImageUpload(requireContext())
        dialog.show()
        dialog.ivImageLoaded?.setImageBitmap(bitmap)
        dialog.bCloseDialog?.setOnClickListener {
            dialog.dismiss()
        }
        dialog.bUploadImage?.setOnClickListener {
            //presenter?.callSaveLocalImage(bitmap)
            dialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //presenter?.callActivityResult(requestCode, resultCode, data)
    }

    private fun isWriteStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (context?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERM", "Permission is granted")
                true
            } else {
                Log.v("PERM", "Permission is revoked")
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 2)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PERM", "Permission is granted")
            true
        }
    }

}