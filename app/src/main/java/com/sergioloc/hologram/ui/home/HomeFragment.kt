package com.sergioloc.hologram.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.needle.app.utils.extensions.gone
import com.needle.app.utils.extensions.setOnSingleClickListener
import com.needle.app.utils.extensions.visible
import com.sergioloc.hologram.R
import com.sergioloc.hologram.ui.player.PlayerActivity
import com.sergioloc.hologram.ui.adapters.HologramAdapter
import com.sergioloc.hologram.databinding.FragmentHomeBinding
import com.sergioloc.hologram.utils.Constants

class HomeFragment: Fragment(), HologramAdapter.OnNewsClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: HologramAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initVariables()
        initButtons()
        initObservers()

        binding.loader.visible()
        viewModel.getNews()
    }

    private fun initView() {
        // Toolbar
        val activity = activity as AppCompatActivity
        activity.title = resources.getString(R.string.home)

        // RecyclerView
        adapter = HologramAdapter(ArrayList(), this)
        binding.rvNews.layoutManager = LinearLayoutManager(context)
        binding.rvNews.adapter = adapter
    }

    private fun initVariables() {
        viewModel = HomeViewModel()
    }

    private fun initButtons() {
        binding.clDemo.setOnSingleClickListener {
            val i = Intent(context, PlayerActivity::class.java)
            i.putExtra("videoId", Constants.DEMO_URL)
            startActivity(i)
        }
    }

    private fun initObservers() {
        viewModel.news.observe(this) {
            it.onSuccess { list ->
                adapter.updateList(list)
            }
            binding.loader.gone()
        }
    }

    override fun onClickNews(url: String) {
        val i = Intent(context, PlayerActivity::class.java)
        i.putExtra("videoId", url)
        startActivity(i)
    }

}