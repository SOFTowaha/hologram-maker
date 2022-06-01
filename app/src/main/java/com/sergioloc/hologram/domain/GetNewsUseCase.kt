package com.sergioloc.hologram.domain

import com.sergioloc.hologram.data.NewsRepository
import com.sergioloc.hologram.domain.model.Hologram
import com.sergioloc.hologram.domain.model.toData
import com.sergioloc.hologram.domain.model.toNewsData
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend operator fun invoke(): List<Hologram> {
        var news: List<Hologram>

        news = repository.getNewsFromFirebase()
        if (news.isEmpty())
            news = repository.getNewsFromDatabase()
        else {
            repository.deleteNewsFromDatabase()
            repository.insertNewsInDatabase(news.map { it.toNewsData() })
        }

        return news
    }

}