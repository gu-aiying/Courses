package com.example.data.network.models

import com.google.gson.annotations.SerializedName

data class NetworkCourseResponse(
    val courses: List<NetworkCourse>
)
data class NetworkCourse(
    val id: Long,
    val title: String,
    @SerializedName("text") val description: String,
    val price: String,
    @SerializedName("rate") val rating: String,
    val startDate: String,
    val hasLike: Boolean,
    val publishDate: String
)
