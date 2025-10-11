package com.example.domain.repository

import com.example.domain.models.Course
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteCourses(): Flow<List<Course>>
}