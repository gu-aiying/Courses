package com.example.presentation_favorites.ui

import com.example.domain.models.Course


sealed class FavoritesState {
    object Loading : FavoritesState()
    data class Success(val courses: List<Course>) : FavoritesState()
    object Empty : FavoritesState()
    data class Error(val message: String) : FavoritesState()

}