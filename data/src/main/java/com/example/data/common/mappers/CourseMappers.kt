package com.example.data.common.mappers

import androidx.room.Insert
import com.example.data.database.entities.FavoriteCourseEntity
import com.example.data.network.models.NetworkCourse
import com.example.domain.models.Course
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


fun NetworkCourse.toDomain(isFavorite: Boolean = false): Course {
    return Course(
        id = this.id,
        title = this.title,
        description = this.description,
        price = this.price,
        rating = this.rating.toDouble(),
        startDate = this.startDate,
        isFavorite = isFavorite,
        publishDate = this.publishDate
    )
}

fun FavoriteCourseEntity.toCourse(): Course {
    return Course(
        id = this.courseId,
        title = this.title,
        description = this.description,
        price = this.price,
        rating = this.rating,
        startDate = this.startDate,
        isFavorite = true,
        publishDate = this.publishDate
    )
}

fun Flow<List<FavoriteCourseEntity>>.toCourses(): Flow<List<Course>> =
    map { entities -> entities.map { it.toCourse() } }

fun Course.toFavoriteEntity(): FavoriteCourseEntity {
    return FavoriteCourseEntity(
        courseId = this.id,
        title = this.title,
        description = this.description,
        price = this.price,
        rating = this.rating,
        startDate = this.startDate,
        publishDate = this.publishDate
    )
}
