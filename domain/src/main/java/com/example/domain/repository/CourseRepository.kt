package com.example.domain.repository

import com.example.domain.models.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    suspend fun getCourses(): List<Course>
    suspend fun toggleFavorite(courseId: String)
    suspend fun setSortingEnabled(enabled: Boolean)
    suspend fun isSortingEnabled(): Boolean
    suspend fun isFavorite(courseId: String): Boolean
    suspend fun refreshCourses()
}