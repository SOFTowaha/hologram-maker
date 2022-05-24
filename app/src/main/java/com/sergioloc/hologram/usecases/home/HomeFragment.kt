package com.sergioloc.hologram.usecases.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.needle.app.utils.extensions.gone
import com.needle.app.utils.extensions.visible
import com.sergioloc.hologram.R
import com.sergioloc.hologram.usecases.player.PlayerActivity
import com.sergioloc.hologram.adapter.NewsAdapter
import com.sergioloc.hologram.databinding.FragmentHomeBinding

class HomeFragment: Fragment(), NewsAdapter.OnNewsClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initVariables()
        initObservers()

        binding.loader.visible()
        viewModel.getNews()
    }

    private fun initView() {
        // Toolbar
        val activity = activity as AppCompatActivity
        activity.title = resources.getString(R.string.news)

        // RecyclerView
        adapter = NewsAdapter(ArrayList(), this)
        binding.rvNews.layoutManager = LinearLayoutManager(context)
        binding.rvNews.adapter = adapter
    }

    private fun initVariables() {
        viewModel = HomeViewModel()
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
        i.putExtra("id", url)
        startActivity(i)
    }

    /*
    "Animals" -> tvTag1.setTextColor(resources.getColor(R.color.orange))
                    "Films" -> tvTag1.setTextColor(resources.getColor(R.color.blue))
                    "Space" -> tvTag1.setTextColor(resources.getColor(R.color.pink))
                    "Nature" -> tvTag1.setTextColor(resources.getColor(R.color.green))
                    "Music" -> tvTag1.setTextColor(resources.getColor(R.color.cyan))
                    "Figures" -> tvTag1.setTextColor(resources.getColor(R.color.yellow))
                    "Others" -> tvTag1.setTextColor(resources.getColor(R.color.purple))
     */
}