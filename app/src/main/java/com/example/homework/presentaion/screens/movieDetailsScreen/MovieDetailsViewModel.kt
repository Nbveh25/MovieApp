package com.example.homework.presentaion.screens.movieDetailsScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework.data.exception.ApiException
import com.example.homework.domain.model.MovieDetails
import com.example.homework.domain.usecase.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetails: GetMovieDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<MovieDetailsState>(MovieDetailsState.Loading)
    val state: StateFlow<MovieDetailsState> = _state.asStateFlow()

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    init {
        loadDetails()
    }

    fun loadDetails() {
        viewModelScope.launch {
            _state.value = MovieDetailsState.Loading
            try {
                val details = getMovieDetails(movieId)
                _state.value = MovieDetailsState.Success(details)
            } catch (e: ApiException) {
                _state.value = MovieDetailsState.Error(
                    when (e.code) {
                        400 -> "Неверный запрос. Проверьте параметры запроса."
                        401 -> "Ошибка авторизации. Проверьте API ключ."
                        404 -> "Фильм не найден."
                        else -> "Ошибка сервера: ${e.code}"
                    }
                )
            } catch (e: Exception) {
                _state.value = MovieDetailsState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}

sealed class MovieDetailsState {
    object Loading : MovieDetailsState()
    data class Success(val movie: MovieDetails) : MovieDetailsState()
    data class Error(val message: String) : MovieDetailsState()
}