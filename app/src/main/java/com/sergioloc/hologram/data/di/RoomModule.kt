package com.sergioloc.hologram.data.di

import android.content.Context
import androidx.room.Room
import com.sergioloc.hologram.data.database.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DATABASE_NAME = "hologram_maker"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, LocalDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideNewsDao(db: LocalDatabase) = db.getNewsDao()

    @Singleton
    @Provides
    fun provideCatalogDao(db: LocalDatabase) = db.getCatalogDao()

    @Singleton
    @Provides
    fun provideGalleryDao(db: LocalDatabase) = db.getGalleryDao()

}