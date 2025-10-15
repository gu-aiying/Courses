package com.example.domain.usecases

import com.example.domain.models.Course
import javax.inject.Inject

class SortCoursesUseCase  @Inject constructor() {
    operator fun invoke(courses: List<Course>, sortByPublishDate: Boolean): List<Course> {
        return if (sortByPublishDate) {
            courses.sortedByDescending { it.publishDate }
        } else {
            courses
        }
    }
}