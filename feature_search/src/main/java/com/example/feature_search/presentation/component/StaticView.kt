package com.example.feature_search.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.domain.model.Movie
import com.example.feature_search.R

@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        trailingIcon = {
            Icon(
                modifier = Modifier.padding(16.dp),
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_icon)
            )
        },
        placeholder = { Text(text = stringResource(R.string.search)) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (query.isNotBlank()) {
                    onSearch(query)
                }
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        )
    )
}

@Composable
fun MovieList(
    movies: List<Movie>,
    navController: NavController
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(movies.size) { index ->
            MovieItem(
                movie = movies[index],
                modifier = Modifier.fillMaxWidth(),
                onMovieClick = { navController.navigate("movie_details/${movies[index].id}") }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieItem(movie: Movie, modifier: Modifier = Modifier, onMovieClick: () -> Unit) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Постер фильма
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = movie.title,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
            )

            // Информация о фильме
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                // Заголовок и рейтинг
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    RatingBadge(rating = movie.voteAverage)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Описание
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun RatingBadge(rating: Double) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "%.1f".format(rating),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun EmptyState(searchQuery: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (searchQuery.isNotBlank()) stringResource(
                R.string.not_movie_query,
                searchQuery
            )
            else stringResource(R.string.movies_not),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}