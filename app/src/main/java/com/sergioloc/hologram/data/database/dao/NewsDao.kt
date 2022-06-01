package com.sergioloc.hologram.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sergioloc.hologram.data.database.entity.NewsEntity

@Dao
interface NewsDao {

    @Query("SELECT * FROM news")
    suspend fun getNews(): ArrayList<NewsEntity>

    @Insert
    suspend fun insertNews(news: ArrayList<NewsEntity>)

}