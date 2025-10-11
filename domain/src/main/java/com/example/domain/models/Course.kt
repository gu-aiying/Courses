package com.example.domain.models

data class Course(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val rating: Double,
    val startDate: String,
    val isFavorite: Boolean,
    val publishDate: String
)
