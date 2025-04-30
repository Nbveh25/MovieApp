package com.example.homework.domain.repository

interface MovieRepository {
    suspend fun getPopularMovies(): List<Movie>
}