package com.sergioloc.hologram.usecases.catalog

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.needle.app.utils.extensions.gone
import com.needle.app.utils.extensions.visible
import com.sergioloc.hologram.R
import com.sergioloc.hologram.adapter.NewsAdapter
import com.sergioloc.hologram.databinding.FragmentCatalogBinding
import com.vpaliy.chips_lover.ChipView

class CatalogFragment: Fragment(), NewsAdapter.OnNewsClickListener {

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

        viewModel.getVideos()
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
            if (binding.chip0.isActivated) {
                deselectChip(binding.chip0)
            } else {
                selectChip(binding.chip0, R.color.red)
                deselectChip(binding.chip1)
                deselectChip(binding.chip2)
                deselectChip(binding.chip3)
                deselectChip(binding.chip4)
                deselectChip(binding.chip5)
                deselectChip(binding.chip6)
                deselectChip(binding.chip7)
            }
        }
        binding.chip1.setOnClickListener {
            if (binding.chip1.isActivated)
                deselectChip(binding.chip1)
            else
                selectChip(binding.chip1, R.color.orange)
        }
        binding.chip2.setOnClickListener {
            if (binding.chip2.isActivated)
                deselectChip(binding.chip2)
            else
                selectChip(binding.chip2, R.color.blue)
        }
        binding.chip3.setOnClickListener {
            if (binding.chip3.isActivated)
                deselectChip(binding.chip3)
            else
                selectChip(binding.chip3, R.color.pink)
        }
        binding.chip4.setOnClickListener {
            if (binding.chip4.isActivated)
                deselectChip(binding.chip4)
            else
                selectChip(binding.chip4, R.color.green)
        }
        binding.chip5.setOnClickListener {
            if (binding.chip5.isActivated)
                deselectChip(binding.chip5)
            else
                selectChip(binding.chip5, R.color.cyan)
        }
        binding.chip6.setOnClickListener {
            if (binding.chip6.isActivated)
                deselectChip(binding.chip6)
            else
                selectChip(binding.chip6, R.color.yellow)
        }
        binding.chip7.setOnClickListener {
            if (binding.chip7.isActivated)
                deselectChip(binding.chip7)
            else
                selectChip(binding.chip7, R.color.purple)
        }

        binding.clBar.setOnClickListener {
            showTags(!tagsOpen)
            tagsOpen = !tagsOpen
        }
    }

    private fun initObservers() {
        viewModel.catalog.observe(this) {
            it.onSuccess { holograms ->
                binding.rvVideos.adapter = NewsAdapter(holograms, this)
            }
        }
    }

    private fun showLoader(visible: Boolean) {
        if (visible) {
            binding.loading.visible()
            binding.rvVideos.gone()
        }
        else {
            binding.loading.gone()
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

    private fun selectChip(chip: ChipView?, color: Int) {
        chip?.backgroundColor = ContextCompat.getColor(requireContext(), color)
        chip?.textColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
    }

    private fun deselectChip(chip: ChipView?) {
        chip?.backgroundColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
        chip?.textColor = ContextCompat.getColor(requireContext(), R.color.black)
    }

    /** ADAPTER LISTENER **/

    override fun onClickNews(url: String) {

    }

}