package com.example.domain.usecases

import com.example.domain.models.Course
import javax.inject.Inject

class SortCoursesUseCase @Inject constructor() {
    operator fun invoke(courses: List<Course>): List<Course> {
        return courses.sortedByDescending {
            it.publishDate.replace("-", "").toInt()
        }
    }
}





