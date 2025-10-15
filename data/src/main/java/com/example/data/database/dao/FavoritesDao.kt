package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.database.entities.FavoriteCourseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorite_courses")
    fun getAllFavorites(): Flow<List<FavoriteCourseEntity>>

    @Insert
    suspend fun addToFavorites(favorite: FavoriteCourseEntity)

    @Query("DELETE FROM favorite_courses WHERE courseId = :courseId")
    suspend fun removeFavoriteById(courseId: Long)

    @Query("SELECT COUNT(*) FROM favorite_courses WHERE courseId = :courseId")
    suspend fun isFavorite(courseId: Long): Int
}