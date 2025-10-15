package com.example.domain.repository

import com.example.domain.models.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    suspend fun getCourses(): List<Course>
    suspend fun toggleFavorite(course: Course)
//    suspend fun setSortingEnabled(enabled: Boolean)
//    suspend fun isSortingEnabled(): Boolean
//    suspend fun isFavorite(courseId: Long): Boolean
    fun getFavoriteCourses(): Flow<List<Course>>
}