package com.sergioloc.hologram.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.needle.app.utils.extensions.gone
import com.needle.app.utils.extensions.setOnSingleClickListener
import com.needle.app.utils.extensions.visible
import com.sergioloc.hologram.R
import com.sergioloc.hologram.ui.player.PlayerActivity
import com.sergioloc.hologram.ui.adapters.HologramAdapter
import com.sergioloc.hologram.databinding.FragmentHomeBinding
import com.sergioloc.hologram.utils.Constants
import com.sergioloc.hologram.utils.Local
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: Fragment(), HologramAdapter.OnNewsClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var adapter: HologramAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVariables()
        initView()
        initButtons()
        initObservers()

        binding.loader.visible()
        viewModel.getNews()
    }

    private fun initVariables() {
        // Init remote config from Firebase
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf(
            "youtube_api_key" to Local.DEFAULT_YOUTUBE_API_KEY
        ))
        remoteConfig.fetchAndActivate().addOnSuccessListener { _ ->
            Constants.YOUTUBE_API_KEY = remoteConfig.getString("youtube_api_key")
        }
    }

    private fun initView() {
        // Toolbar
        val activity = activity as AppCompatActivity
        activity.title = resources.getString(R.string.home)

        // RecyclerView
        adapter = HologramAdapter(ArrayList(), this)
        binding.rvNews.layoutManager = LinearLayoutManager(context)
        binding.rvNews.adapter = adapter

        // Ad
        binding.adBanner.loadAd(AdRequest.Builder().build())
    }

    private fun initButtons() {
        binding.clDemo.setOnSingleClickListener {
            val i = Intent(context, PlayerActivity::class.java)
            i.putExtra("videoId", Constants.DEMO_URL)
            startActivity(i)
        }
    }

    private fun initObservers() {
        viewModel.news.observe(viewLifecycleOwner) {
            it.onSuccess { list ->
                if (list.isEmpty())
                    binding.tvConnection.visible()
                else {
                    binding.tvConnection.gone()
                    adapter.updateList(list)
                }
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