package com.example.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MovieListDto(
    @SerializedName("results")
    val results: List<MovieDto>
)