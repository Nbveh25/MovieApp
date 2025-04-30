package com.example.homework.domain.usecase

import com.example.homework.domain.model.Movie
import com.example.homework.domain.repository.MovieRepository
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): List<Movie> {
        return repository.getPopularMovies()
    }
}