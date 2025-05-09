package com.example.feature_search.presentation.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Movie
import com.example.domain.usecase.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchListViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state: StateFlow<SearchState> = _state.asStateFlow()

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _state.value = SearchState.Loading
            try {
                //Log.d("SearchListViewModel", "Searching for: $query")
                val movies = searchMoviesUseCase.invoke(query)
                //Log.d("SearchListViewModel", "Found ${movies.size} movies")
                _state.value = if (movies.isEmpty()) {
                    SearchState.Empty
                } else {
                    SearchState.Success(movies)
                }
            } catch (e: Exception) {
                //Log.e("SearchListViewModel", "Error searching movies", e)
                _state.value = SearchState.Error("Ошибка при поиске: ${e.message}")
            }
        }
    }
}

sealed class SearchState {
    object Initial : SearchState()
    object Loading : SearchState()
    data class Success(val movies: List<Movie>) : SearchState()
    data class Error(val message: String) : SearchState()
    object Empty : SearchState()
}

