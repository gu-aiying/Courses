package com.example.domain.usecases
import com.example.domain.models.Course
import com.example.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCoursesUseCase @Inject constructor (
    private val repository: CourseRepository
)  {
    suspend operator fun invoke(): Flow<List<Course>> {
        return repository.getCourses()
    }
}