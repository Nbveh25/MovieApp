package com.example.domain.usecase

import com.example.domain.enums.Source
import com.example.domain.model.MovieDetails
import com.example.domain.repository.MovieRepository

class GetMovieDetailsUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Pair<MovieDetails, Source> {
        return repository.getMovieDetails(movieId)
    }
}