package com.example.homework.presentaion.screens.movieListScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getPopularMovies: GetPopularMoviesUseCase
) : ViewModel() {

    private val _state = mutableStateOf<MovieListState>(MovieListState.Loading)
    val state: State<MovieListState> = _state

    fun loadMovies() {
        viewModelScope.launch {
            _state.value = MovieListState.Loading
            try {
                val movies = getPopularMovies()
                _state.value = MovieListState.Success(movies)
            } catch (e: ForbiddenException) {
                _state.value = MovieListState.Error("Включите VPN!")
            } catch (e: NetworkException) {
                _state.value = MovieListState.Error("Нет интернета")
            }
        }
    }
}

sealed class MovieListState {
    object Loading : MovieListState()
    data class Success(val movies: List<Movie>) : MovieListState()
    data class Error(val message: String) : MovieListState()
}