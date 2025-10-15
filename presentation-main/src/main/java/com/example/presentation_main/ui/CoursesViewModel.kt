package com.example.presentation_main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Course
import com.example.domain.usecases.GetCoursesUseCase
import com.example.domain.usecases.ToggleFavoriteUseCase
import com.example.domain.usecases.SortCoursesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val sortCoursesUseCase: SortCoursesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CoursesState>(CoursesState.Loading)
    val state: StateFlow<CoursesState> = _state.asStateFlow()

    private var isSortedByPublishDate = false

    init {
        loadCourses()
    }

    fun loadCourses() {
        _state.value = CoursesState.Loading

        viewModelScope.launch {
            try {
                getCoursesUseCase().collect { courses ->
                    updateCoursesState(courses)
                }
            } catch (e: Exception) {
                _state.value = CoursesState.Error("Не удалось загрузить курсы: ${e.message}")
            }
        }
    }

    fun toggleFavorite(course: Course) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(course)
            } catch (e: Exception) {
                _state.value = CoursesState.Error("Не удалось обновить избранное: ${e.message}")
            }
        }
    }

    fun toggleSorting() {
        isSortedByPublishDate = !isSortedByPublishDate
        val currentCourses = when (val currentState = _state.value) {
            is CoursesState.Success -> currentState.courses
            else -> emptyList()
        }
        updateCoursesState(currentCourses)
    }

    private fun updateCoursesState(courses: List<Course>) {
        val coursesToShow = if (isSortedByPublishDate) {
            sortCoursesUseCase(courses, true)
        } else {
            courses
        }

        _state.update {
            if (coursesToShow.isEmpty()) {
                CoursesState.Empty
            } else {
                CoursesState.Success(
                    courses = coursesToShow,
                    isSorted = isSortedByPublishDate
                )
            }
        }
    }

}