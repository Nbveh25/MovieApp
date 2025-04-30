package com.example.homework.presentaion.screens.movieListScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework.domain.model.Movie
import com.example.homework.domain.usecase.GetPopularMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getPopularMovies: GetPopularMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<MovieListState>(MovieListState.Loading)
    val state: StateFlow<MovieListState> = _state.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _state.value = MovieListState.Loading
            try {
                val movies = getPopularMovies()
                _state.value = MovieListState.Success(movies)
            } catch (e: Exception) {
                _state.value = MovieListState.Error("Ошибка")
            }
        }
    }
}

sealed class MovieListState {
    object Loading : MovieListState()
    data class Success(val movies: List<Movie>) : MovieListState()
    data class Error(val message: String) : MovieListState()
}