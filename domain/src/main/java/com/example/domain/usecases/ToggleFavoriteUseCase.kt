package com.example.domain.usecases

import com.example.domain.repository.CourseRepository
import javax.inject.Inject

class ToggleFavoriteUseCase@Inject constructor(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(courseId: String) {
        repository.toggleFavorite(courseId)
    }
}