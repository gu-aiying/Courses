package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.database.AppDatabase
import com.example.data.database.dao.FavoritesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "courses_database"
        )
            .fallbackToDestructiveMigration(true) // Временно, пока не написаны миграции
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoritesDao(database: AppDatabase): FavoritesDao {
        return database.favoritesDao()
    }
}