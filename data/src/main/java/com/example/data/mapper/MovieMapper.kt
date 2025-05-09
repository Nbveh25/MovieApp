package com.example.data.mapper

import com.example.data.remote.dto.MovieDetailsDto
import com.example.data.remote.dto.MovieDto
import com.example.domain.model.Genre
import com.example.domain.model.Movie
import com.example.domain.model.MovieDetails

object MovieMapper {
    fun mapToDomain(dto: MovieDto): Movie = Movie(
        id = dto.id,
        title = dto.title,
        overview = dto.overview,
        posterPath = dto.posterPath,
        releaseDate = dto.releaseDate,
        voteAverage = dto.voteAverage
    )

    fun mapToDomain(dto: MovieDetailsDto): MovieDetails = MovieDetails(
        id = dto.id,
        title = dto.title,
        overview = dto.overview,
        posterPath = dto.posterPath,
        runtime = dto.runtime,
        voteAverage = dto.voteAverage,
        genres = dto.genres.map { Genre(it.id, it.name) },
        releaseDate = dto.releaseDate
    )


}