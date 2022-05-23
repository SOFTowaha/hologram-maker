package com.sergioloc.hologram.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergioloc.hologram.Interfaces.HomeInterface
import com.sergioloc.hologram.Views.PlayerActivity
import com.sergioloc.hologram.adapter.NewsAdapter
import com.sergioloc.hologram.databinding.FragmentHomeBinding
import com.sergioloc.hologram.models.News

class HomeFragment: HomeInterface.View, Fragment(), NewsAdapter.OnNewsClickListener {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val news = arrayListOf(
            News("1", "", "", ""),
            News("2", "", "", ""),
            News("3", "", "", ""),
            News("4", "", "", "")
        )

        binding.rvNews.layoutManager = LinearLayoutManager(context)
        binding.rvNews.adapter = NewsAdapter(news, this)
    }

    override fun navigateToActivity(code: String) {
        val i = Intent(context, PlayerActivity::class.java)
        i.putExtra("id", code)
        startActivity(i)
    }

    override fun showNew(image: Uri, name: String, tag: String, position: Int) {

    }

    override fun onClickNews(url: String) {

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