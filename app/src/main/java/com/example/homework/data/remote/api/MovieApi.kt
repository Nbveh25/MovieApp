package com.example.homework.data.remote.api

import com.example.homework.data.response.MovieResponse
import retrofit2.http.GET

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(): MovieResponse
}