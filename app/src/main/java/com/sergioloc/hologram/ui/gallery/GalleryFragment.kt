package com.sergioloc.hologram.ui.gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
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
import androidx.recyclerview.widget.GridLayoutManager
import com.needle.app.utils.extensions.gone
import com.needle.app.utils.extensions.setOnSingleClickListener
import com.needle.app.utils.extensions.visible
import com.sergioloc.hologram.R
import com.sergioloc.hologram.ui.adapters.GalleryAdapter
import com.sergioloc.hologram.databinding.FragmentGalleryBinding
import com.sergioloc.hologram.ui.dialogs.GalleryDialog
import com.sergioloc.hologram.ui.square.SquareActivity
import com.sergioloc.hologram.utils.Constants
import com.sergioloc.hologram.utils.Session
import java.io.*
import kotlin.collections.ArrayList

class GalleryFragment: Fragment(), GalleryAdapter.OnHologramClickListener {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var viewModel: GalleryViewModel
    private lateinit var adapter: GalleryAdapter
    private lateinit var prefs: SharedPreferences
    private var length = 0
    private var nextId = 0

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

        binding.loader.visible()
        viewModel.getMyHolograms(requireContext(), length)
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
    }

    private fun initVariables() {
        viewModel = GalleryViewModel()

        prefs = requireActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
        length = prefs.getInt(Constants.PREF_LENGTH, 0)
        nextId = prefs.getInt(Constants.PREF_NEXT_ID, 1)
    }

    private fun initObservers() {
        viewModel.list.observe(this) {
            it.onSuccess { list ->
                showLoader(false)
                if (list.isEmpty())
                    binding.tvEmpty.visible()

                adapter.updateList(list)
            }
        }

        viewModel.newImage.observe(this) {
            it.onSuccess { bitmap ->
                showLoader(false)
                adapter.addItem(bitmap)
                with (prefs.edit()) {
                    length++
                    nextId++
                    putInt(Constants.PREF_LENGTH, length)
                    putInt(Constants.PREF_NEXT_ID, nextId)
                    apply()
                }
            }
        }

        viewModel.deleteImage.observe(this) {
            it.onSuccess { position ->
                showLoader(false)
                adapter.removeItem(position)
                with (prefs.edit()) {
                    length--
                    putInt(Constants.PREF_LENGTH, length)
                    apply()
                }
                if (length < 1)
                    binding.tvEmpty.visible()
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
                        viewModel.saveNewHologram(requireContext(), bitmap, nextId)
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

    override fun onHologramClick(bitmap: Bitmap, position: Int) {
        val dialog = GalleryDialog(requireContext())
        dialog.apply {
            setBitmap(bitmap)
            setOnDeleteClickListener {
                showLoader(true)
                viewModel.deleteHologram(requireContext(), position)
            }
            setOnHologramClickListener {
                Session.bitmap = bitmap
                startActivity(Intent(requireActivity(), SquareActivity::class.java))
            }
            show()
        }
    }

}