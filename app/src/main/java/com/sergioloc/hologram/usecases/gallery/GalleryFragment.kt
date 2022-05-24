package com.sergioloc.hologram.usecases.gallery

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.needle.app.utils.extensions.setOnSingleClickListener
import com.sergioloc.hologram.R
import com.sergioloc.hologram.adapter.GalleryAdapter
import com.sergioloc.hologram.databinding.FragmentGalleryBinding
import com.sergioloc.hologram.dialogs.DialogImageUpload
import com.sergioloc.hologram.utils.Constants
import kotlinx.android.synthetic.main.dialog_image_upload.*
import java.io.*
import kotlin.collections.ArrayList

class GalleryFragment: Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var viewModel: GalleryViewModel
    private lateinit var adapter: GalleryAdapter
    private lateinit var prefs: SharedPreferences
    private var size = 0

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

        viewModel.getMyHolograms(requireContext(), size)
    }

    private fun initView() {
        // Toolbar
        val activity = activity as AppCompatActivity
        activity.title = resources.getString(R.string.myholograms)

        // RecyclerView
        adapter = GalleryAdapter(ArrayList())
        binding.rvImages.setHasFixedSize(true)
        binding.rvImages.layoutManager = GridLayoutManager(context, 3)
        binding.rvImages.adapter = adapter
    }

    private fun initVariables() {
        viewModel = GalleryViewModel()

        prefs = requireActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE)
        size = prefs.getInt("size", 0)
    }

    private fun initObservers() {
        viewModel.list.observe(this) {
            it.onSuccess { list ->
                adapter.updateList(list)
            }
        }

        viewModel.newImage.observe(this) {
            it.onSuccess { bitmap ->
                adapter.addItem(bitmap)
                with (prefs.edit()) {
                    size++
                    putInt("size", size)
                    apply()
                }
            }
        }
    }

    private fun initButtons() {
        binding.btnAdd.setOnSingleClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startForResult.launch(photoPickerIntent)
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

    private var startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val uri: Uri? = result?.data?.data
                uri?.let {
                    val inputStream: InputStream? = context?.contentResolver?.openInputStream(it)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    Thread {
                        viewModel.saveNewHologram(requireContext(), bitmap, adapter.itemCount+1)
                    }.start()
                }
            } catch (e: Exception) {
                Log.e(Constants.TAG_GALLERY, "Error loading image from gallery")
                Toast.makeText(context, getString(R.string.error_loading_image), Toast.LENGTH_SHORT).show()
            }
        }
    }

}