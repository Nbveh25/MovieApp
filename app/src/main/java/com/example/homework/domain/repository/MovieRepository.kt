package com.example.homework.domain.repository

import com.example.homework.domain.model.Movie
import com.example.homework.domain.model.MovieDetails

interface MovieRepository {
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getMovieDetails(movieId: Int): MovieDetails
}