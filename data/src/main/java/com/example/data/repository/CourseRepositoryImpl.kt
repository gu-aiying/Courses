package com.example.data.repository

import com.example.data.common.mappers.toCourses
import com.example.data.common.mappers.toDomain
import com.example.data.common.mappers.toFavoriteEntity
import com.example.data.database.dao.FavoritesDao
import com.example.data.network.api.CoursesApi
import com.example.domain.models.Course
import com.example.domain.repository.CourseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    private val coursesApi: CoursesApi,
    private val favoritesDao: FavoritesDao,
) : CourseRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getCourses(): Flow<List<Course>> =
        combine(
            flow { emit(coursesApi.getCourses().courses) }, // один раз запроса
            favoritesDao.getAllFavoritesID() // следить за изменение избранного
        ) { networkCourses, favoriteIds ->
            val favoriteIdsSet = favoriteIds.toSet()
            networkCourses.map { networkCourse ->
                networkCourse.toDomain(isFavorite = favoriteIdsSet.contains(networkCourse.id))
            }
        }


    override suspend fun toggleFavorite(course: Course) {
        val isCurrentlyFavorite = favoritesDao.isFavorite(course.id) > 0
        if (isCurrentlyFavorite) {
            favoritesDao.removeFavoriteById(course.id)
        } else {
            favoritesDao.addToFavorites(course.toFavoriteEntity())
        }
    }

    override fun getFavoriteCourses(): Flow<List<Course>> {
        return favoritesDao.getAllFavorites().toCourses()
    }
}