package com.sergioloc.hologram.usecases.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sergioloc.hologram.Views.PlayerActivity
import com.sergioloc.hologram.adapter.NewsAdapter
import com.sergioloc.hologram.databinding.FragmentHomeBinding
import com.sergioloc.hologram.models.News

class HomeFragment: Fragment(), NewsAdapter.OnNewsClickListener {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val news = arrayListOf(
            News("Video 1", "https://docs.microsoft.com/es-es/windows/apps/design/controls/images/image-licorice.jpg", "Tag 1", ""),
            News("Video 2", "https://thumbs.dreamstime.com/b/profil-de-hibou-16485620.jpg", "Tag 2", ""),
            News("Video 3", "https://tinypng.com/images/smart-resizing/new-aspect-ratio.jpg", "Tag 3", ""),
            News("Video 4", "https://www.dcode.fr/tools/image-randomness/images/random-dcode.png", "Tag 4", ""),
            News("Video 5", "https://res.cloudinary.com/demo/image/upload/w_500,f_auto/sample", "Tag 5", ""),
            News("Video 6", "", "Tag 6", ""),
            News("Video 7", "", "Tag 7", ""),
            News("Video 8", "", "Tag 8", ""),
            News("Video 9", "", "Tag 9", ""),
            News("Video 10", "", "Tag10", "")
        )

        binding.rvNews.layoutManager = LinearLayoutManager(context)
        binding.rvNews.adapter = NewsAdapter(news, this)
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