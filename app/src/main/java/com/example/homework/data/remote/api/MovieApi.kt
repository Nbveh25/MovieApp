package com.example.homework.data.remote.api

import com.example.homework.data.response.MovieDetailsDto
import com.example.homework.data.response.MovieDto
import com.example.homework.data.response.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(): MovieListResponse

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int
    ): MovieDetailsDto
}