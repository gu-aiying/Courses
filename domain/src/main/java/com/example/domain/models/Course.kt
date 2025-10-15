package com.example.domain.models

data class Course(
    val id: Long,
    val title: String,
    val image: String? = null,
    val description: String,
    val price: String,
    val rating: Double,
    val startDate: String,
    val isFavorite: Boolean,
    val publishDate: String
)
