package com.example.presentation_favorites.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Course
import com.example.domain.usecases.GetFavoriteCoursesUseCase
import com.example.domain.usecases.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteCoursesUseCase: GetFavoriteCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<FavoritesState>(FavoritesState.Loading)
    val state: StateFlow<FavoritesState> = _state.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            try {
                getFavoriteCoursesUseCase().collectLatest { courses ->
                    if (courses.isEmpty()) {
                        _state.value = FavoritesState.Empty
                    } else {
                        _state.value = FavoritesState.Success(courses)
                    }
                }
            } catch (e: Exception) {
                _state.value = FavoritesState.Error("Не удалось загрузить курсы: ${e.message}")
            }

        }
    }

    fun toggleFavorite(course: Course) {
        viewModelScope.launch {
            toggleFavoriteUseCase(course)
        }
    }

    fun refresh() {
        _state.value = FavoritesState.Loading
        loadFavorites()
    }
}