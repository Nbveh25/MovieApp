package com.example.homework.presentaion.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.feature_search.presentation.screen.SearchListScreen
import com.example.homework.presentaion.screens.movieDetailsScreen.MovieDetailsScreen
import com.example.homework.presentaion.screens.movieListScreen.MovieListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.MOVIE_LIST
    ) {
        movieListScreen(navController)
        movieDetailsScreen(navController)
        searchListScreen(navController)
    }
}

private fun NavGraphBuilder.movieListScreen(navController: NavController) {
    composable(route = Destinations.MOVIE_LIST) {
        MovieListScreen(
            navController = navController
        )
    }
}

private fun NavGraphBuilder.movieDetailsScreen(navController: NavController) {
    composable(
        route = Destinations.MOVIE_DETAILS,
        arguments = listOf(navArgument("movieId") {
            type = NavType.IntType
            defaultValue = 0
        })
    ) { backStackEntry ->
        MovieDetailsScreen(
            navController = navController,
            viewModel = hiltViewModel()
        )
    }
}

private fun NavGraphBuilder.searchListScreen(navController: NavController) {
    composable(
        route = Destinations.SEARCH,
    ) { backStackEntry ->
        SearchListScreen(
            viewModel = hiltViewModel(),
            navController = navController
        )
    }
}
