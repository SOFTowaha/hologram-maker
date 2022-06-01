package com.sergioloc.hologram.ui.gallery

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdRequest
import com.needle.app.utils.extensions.gone
import com.needle.app.utils.extensions.setOnSingleClickListener
import com.needle.app.utils.extensions.visible
import com.sergioloc.hologram.R
import com.sergioloc.hologram.ui.adapters.GalleryAdapter
import com.sergioloc.hologram.databinding.FragmentGalleryBinding
import com.sergioloc.hologram.domain.model.Gallery
import com.sergioloc.hologram.ui.dialogs.GalleryDialog
import com.sergioloc.hologram.ui.square.SquareActivity
import com.sergioloc.hologram.utils.Constants
import com.sergioloc.hologram.utils.Session
import dagger.hilt.android.AndroidEntryPoint
import java.io.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class GalleryFragment: Fragment(), GalleryAdapter.OnHologramClickListener {

    private lateinit var binding: FragmentGalleryBinding
    private val viewModel: GalleryViewModel by viewModels()
    private lateinit var adapter: GalleryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObservers()
        initButtons()

        binding.loader.visible()
        viewModel.getMyImages()
    }

    private fun initView() {
        // Toolbar
        val activity = activity as AppCompatActivity
        activity.title = resources.getString(R.string.myholograms)

        // RecyclerView
        adapter = GalleryAdapter(ArrayList(), this)
        binding.rvImages.setHasFixedSize(true)
        binding.rvImages.layoutManager = GridLayoutManager(context, 3)
        binding.rvImages.adapter = adapter

        // Ad
        binding.adBanner.loadAd(AdRequest.Builder().build())
    }

    private fun initObservers() {
        viewModel.images.observe(viewLifecycleOwner) {
            it.onSuccess { list ->
                showLoader(false)
                if (list.isEmpty())
                    binding.tvEmpty.visible()

                adapter.updateList(list)
            }
        }

        viewModel.newImage.observe(viewLifecycleOwner) {
            it.onSuccess {
                showLoader(false)
                viewModel.getMyImages()
            }
        }

        viewModel.deleteImage.observe(viewLifecycleOwner) {
            it.onSuccess {
                showLoader(false)
                viewModel.getMyImages()
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

    private var startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val uri: Uri? = result?.data?.data
                uri?.let {
                    showLoader(true)
                    val inputStream: InputStream? = context?.contentResolver?.openInputStream(it)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    Thread {
                        viewModel.saveNewImage(bitmap)
                    }.start()
                }
            } catch (e: Exception) {
                Log.e(Constants.TAG_GALLERY, "Error loading image from gallery")
                Toast.makeText(context, getString(R.string.error_loading_image), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoader(visible: Boolean) {
        if (visible) {
            binding.loader.visible()
            binding.tvCreating.visible()
            binding.ivWhite.visible()
            binding.tvEmpty.gone()
        }
        else {
            binding.loader.gone()
            binding.tvCreating.gone()
            binding.ivWhite.gone()
        }
    }

    /** ADAPTER LISTENER **/

    override fun onHologramClick(image: Gallery) {
        val dialog = GalleryDialog(requireContext())
        dialog.apply {
            image.bitmap?.let { setBitmap(it) }
            setOnDeleteClickListener {
                showLoader(true)
                viewModel.deleteImage(image)
            }
            setOnHologramClickListener {
                Session.bitmap = image.bitmap
                startActivity(Intent(requireActivity(), SquareActivity::class.java))
            }
            show()
        }
    }

}