package com.example.homework.data.repository

import com.example.homework.data.remote.api.MovieApi
import com.example.homework.domain.model.Movie
import com.example.homework.domain.repository.MovieRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi
) : MovieRepository {
    override suspend fun getPopularMovies(): List<Movie> {
        return api.getPopularMovies().results.map { it.toDomain() }
    }
}