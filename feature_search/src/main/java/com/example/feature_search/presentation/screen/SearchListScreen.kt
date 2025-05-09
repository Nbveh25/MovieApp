package com.example.feature_search.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.feature_search.R
import com.example.feature_search.presentation.component.EmptyState
import com.example.feature_search.presentation.component.MovieList
import com.example.feature_search.presentation.component.SearchView

@Composable
fun SearchListScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchListViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        SearchView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { viewModel.searchMovies(it) },
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)) {
            when (val currentState = state) {
                is SearchState.Initial -> {
                    EmptyState(searchQuery = searchQuery)
                }
                is SearchState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is SearchState.Success -> {
                    MovieList(
                        movies = currentState.movies,
                        navController = navController
                    )
                }
                is SearchState.Empty -> {
                    Text(
                        text = stringResource(R.string.nothing),
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                is SearchState.Error -> {
                    ErrorMessage(
                        message = currentState.message,
                        modifier = Modifier.align(Alignment.Center),
                        onRetry = { viewModel.searchMovies(searchQuery) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.retry))
        }
    }
}


