package com.example.domain.repository

import com.example.domain.enums.Source
import com.example.domain.model.Movie
import com.example.domain.model.MovieDetails

interface MovieRepository {
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getMovieDetails(movieId: Int): Pair<MovieDetails, Source>
    suspend fun searchMovies(query: String): List<Movie>
}