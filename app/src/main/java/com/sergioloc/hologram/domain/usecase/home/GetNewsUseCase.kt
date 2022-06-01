package com.sergioloc.hologram.domain.usecase.home

import com.sergioloc.hologram.data.NewsRepository
import com.sergioloc.hologram.domain.model.Hologram
import com.sergioloc.hologram.domain.model.toNewsData
import com.sergioloc.hologram.utils.Session
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend operator fun invoke(): List<Hologram> {
        if (Session.newsLoaded)
            return repository.getNewsFromDatabase()

        var news = repository.getNewsFromFirebase()
        if (news.isEmpty())
            news = repository.getNewsFromDatabase()
        else {
            Session.newsLoaded = true
            repository.deleteNewsFromDatabase()
            repository.insertNewsInDatabase(news.map { it.toNewsData() })
        }

        return news
    }

}