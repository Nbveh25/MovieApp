package com.example.homework.presentaion.screens.movieListScreen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MovieListScreen(viewModel: MovieListViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is MovieListState.Loading -> ShimmerLoading()
        is MovieListState.Success -> MovieListContent(currentState.movies)
        is MovieListState.Error -> ErrorScreen(message = currentState.message)
    }
}