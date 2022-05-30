package com.sergioloc.hologram.ui.catalog

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.needle.app.utils.extensions.gone
import com.needle.app.utils.extensions.visible
import com.sergioloc.hologram.R
import com.sergioloc.hologram.ui.adapters.HologramAdapter
import com.sergioloc.hologram.databinding.FragmentCatalogBinding
import com.sergioloc.hologram.ui.player.PlayerActivity

class CatalogFragment: Fragment(), HologramAdapter.OnNewsClickListener {

    private lateinit var binding: FragmentCatalogBinding
    private lateinit var viewModel: CatalogViewModel

    private var tagsOpen = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCatalogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariables()
        initView()
        initButtons()
        initObservers()

        showLoader(true)
        viewModel.getCatalog()
    }

    private fun initVariables() {
        viewModel = CatalogViewModel()
    }

    private fun initView() {
        // Toolbar
        val activity = activity as AppCompatActivity?
        activity?.title = resources.getString(R.string.title_catalog)

        // RecyclerView
        val layoutManager = LinearLayoutManager(context)
        binding.rvVideos.setHasFixedSize(true)
        binding.rvVideos.isNestedScrollingEnabled = false
        binding.rvVideos.layoutManager = layoutManager
    }

    private fun initButtons() {
        binding.chip0.setOnClickListener {
            if (binding.chip0.isChipSelected()) {
                binding.chip1.unselect()
                binding.chip2.unselect()
                binding.chip3.unselect()
                binding.chip4.unselect()
                binding.chip5.unselect()
                binding.chip6.unselect()
                binding.chip7.unselect()
            }
        }
        binding.chip1.setOnClickListener {
            binding.chip0.unselect()
        }
        binding.chip2.setOnClickListener {
            binding.chip0.unselect()
        }
        binding.chip3.setOnClickListener {
            binding.chip0.unselect()
        }
        binding.chip4.setOnClickListener {
            binding.chip0.unselect()
        }
        binding.chip5.setOnClickListener {
            binding.chip0.unselect()
        }
        binding.chip6.setOnClickListener {
            binding.chip0.unselect()
        }
        binding.chip7.setOnClickListener {
            binding.chip0.unselect()
        }

        binding.clBar.setOnClickListener {
            showTags(!tagsOpen)
            tagsOpen = !tagsOpen
        }
    }

    private fun initObservers() {
        viewModel.catalog.observe(this) {
            it.onSuccess { holograms ->
                binding.rvVideos.adapter = HologramAdapter(holograms, this)
                binding.tvCount.text = getString(R.string.num_videos, holograms.size)
            }
            showLoader(false)
        }
    }

    private fun showLoader(visible: Boolean) {
        if (visible) {
            binding.loader.visible()
            binding.rvVideos.gone()
        }
        else {
            binding.loader.gone()
            binding.rvVideos.visible()
        }
    }

    private fun showTags(visible: Boolean) {
       if (visible) {
           binding.chipsLayout.visible()
           binding.ivArrow.setImageResource(R.drawable.ic_arrow_up)
       }
       else {
           binding.chipsLayout.gone()
           binding.ivArrow.setImageResource(R.drawable.ic_arrow_bottom)
       }
    }

    /** ADAPTER LISTENER **/

    override fun onClickNews(url: String) {
        val i = Intent(context, PlayerActivity::class.java)
        i.putExtra("id", url)
        startActivity(i)
    }

}