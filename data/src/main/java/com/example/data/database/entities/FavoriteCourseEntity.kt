package com.example.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_courses")
data class FavoriteCourseEntity(
    @PrimaryKey
    val courseId: Long,

    val title: String,
    val description: String,
    val price: String,
    val rating: Double,
    val startDate: String,
    val publishDate: String
)
