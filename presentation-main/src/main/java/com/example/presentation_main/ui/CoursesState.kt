package com.example.presentation_main.ui

import com.example.domain.models.Course

sealed class CoursesState {
    object Loading : CoursesState()
    data class Success(
        val courses: List<Course>,
        val isSorted: Boolean = false
    ) : CoursesState()
    data class Error(val message: String) : CoursesState()
    object Empty : CoursesState()
}