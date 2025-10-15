package com.example.domain.repository

import com.example.domain.models.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    suspend fun getCourses(): Flow<List<Course>>
    suspend fun toggleFavorite(course: Course)
//    suspend fun isFavorite(courseId: Long): Boolean
    fun getFavoriteCourses(): Flow<List<Course>>
}