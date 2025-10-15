package com.example.data.repository

import com.example.data.common.mappers.toCourses
import com.example.data.common.mappers.toDomain
import com.example.data.common.mappers.toFavoriteEntity
import com.example.data.database.dao.FavoritesDao
import com.example.data.network.api.CoursesApi
import com.example.domain.models.Course
import com.example.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    private val coursesApi: CoursesApi,
    private val favoritesDao: FavoritesDao,
) : CourseRepository {

    private var cachedCourses: List<Course> = emptyList()

    override suspend fun getCourses(): List<Course> {

        val networkCourses = coursesApi.getCourses().courses
        val favoriteIds = favoritesDao.getAllFavorites()
            .first()
            .map { it.courseId }
            .toSet()

        cachedCourses = networkCourses.map { networkCourse ->
            networkCourse.toDomain(isFavorite = favoriteIds.contains(networkCourse.id))
        }


        return cachedCourses

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