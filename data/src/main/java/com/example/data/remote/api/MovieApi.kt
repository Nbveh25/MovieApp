package com.example.data.remote.api

import com.example.data.remote.dto.MovieDetailsDto
import com.example.data.remote.dto.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(): MovieListDto

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int
    ): MovieDetailsDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String
    ) : MovieListDto
}