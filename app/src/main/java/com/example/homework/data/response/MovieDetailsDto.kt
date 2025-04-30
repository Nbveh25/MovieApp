package com.example.homework.data.response

import com.google.gson.annotations.SerializedName

data class MovieDetailsDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("runtime") val runtime: Int,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("genres") val genres: List<GenreDto>,
    @SerializedName("release_date") val releaseDate: String
)