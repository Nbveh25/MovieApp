package com.example.homework.domain.model

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val runtime: Int,
    val voteAverage: Double,
    val genres: List<Genre>,
    val releaseDate: String
)