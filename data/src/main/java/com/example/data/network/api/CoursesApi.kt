package com.example.data.network.api

import com.example.data.network.models.NetworkCourseResponse
import retrofit2.http.GET

interface CoursesApi {
    @GET("u/0/uc?id=15arTK7XT2b7Yv4BJsmDctA4Hg-BbS8-q&export=download")
    suspend fun getCourses(): NetworkCourseResponse
}