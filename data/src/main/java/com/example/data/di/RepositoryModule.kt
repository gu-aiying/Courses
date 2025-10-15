package com.example.data.di

import com.example.data.database.dao.FavoritesDao
import com.example.data.network.api.CoursesApi
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.CourseRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.CourseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCourseRepository(
        coursesApi: CoursesApi,
        favoritesDao: FavoritesDao
    ): CourseRepository {
        return CourseRepositoryImpl(coursesApi, favoritesDao)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return AuthRepositoryImpl()
    }
}