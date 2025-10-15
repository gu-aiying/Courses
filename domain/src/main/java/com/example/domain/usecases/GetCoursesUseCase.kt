package com.example.domain.usecases
import com.example.domain.models.Course
import com.example.domain.repository.CourseRepository
import javax.inject.Inject


class GetCoursesUseCase @Inject constructor (
    private val repository: CourseRepository
)  {
    suspend operator fun invoke(): List<Course> {
        return repository.getCourses()
    }
}