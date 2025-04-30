package com.example.homework.data.mapper

import com.example.homework.data.response.MovieDetailsDto
import com.example.homework.data.response.MovieDto

// data/mapper/MovieMapper.kt
object MovieMapper {
    fun mapToDomain(dto: MovieDto): Movie = Movie(
        id = dto.id,
        title = dto.title,
        overview = dto.overview,
        posterPath = dto.posterPath,
        releaseDate = dto.releaseDate,
        voteAverage = dto.voteAverage
    )

    fun mapDetailsToDomain(dto: MovieDetailsDto): MovieDetails = MovieDetails(
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