package com.example.homework.presentaion.screens.movieListScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.app.R
import com.example.homework.presentaion.component.ErrorScreen
import com.example.homework.presentaion.component.MovieListContent
import com.example.homework.presentaion.component.ShimmerList
import com.example.homework.presentaion.navigation.Destinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    navController: NavController,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.popular)) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("search")
                    }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = stringResource(R.string.search)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(Destinations.GRAPH)
                },
                icon = { Icon(Icons.Default.Add, "Add") },
                text = { Text(stringResource(R.string.add_graphic)) }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val currentState = state.value) {
                is MovieListState.Loading -> ShimmerList()
                is MovieListState.Success -> MovieListContent(
                    movies = currentState.movies,
                    onMovieClick = { movieId ->
                        navController.navigate("movie_details/$movieId")
                    }
                )
                is MovieListState.Error -> ErrorScreen(
                    message = currentState.message,
                    onRetry = viewModel::loadMovies
                )
            }
        }
    }
}