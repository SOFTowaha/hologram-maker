package com.sergioloc.hologram.data

import com.sergioloc.hologram.data.database.dao.NewsDao
import com.sergioloc.hologram.data.database.entity.NewsEntity
import com.sergioloc.hologram.data.firebase.FirebaseService
import com.sergioloc.hologram.domain.model.Hologram
import com.sergioloc.hologram.domain.model.toDomain
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api: FirebaseService,
    private val newsDao: NewsDao
) {

    suspend fun getNewsFromFirebase(): List<Hologram> {
        val response = api.getNews()
        return response.map { it.toDomain() }
    }

    suspend fun getNewsFromDatabase(): List<Hologram> {
        val response = newsDao.getNews()
        return response.map { it.toDomain() }
    }

    suspend fun insertNewsInDatabase(news: List<NewsEntity>) {
       newsDao.insertNews(news)
    }

    suspend fun deleteNewsFromDatabase() {
        newsDao.deleteNews()
    }

}