package com.example.homework.presentaion.screens.movieDetailsScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.enums.Source
import com.example.domain.exception.ApiException
import com.example.domain.model.MovieDetails
import com.example.domain.usecase.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetails: GetMovieDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<MovieDetailsState>(MovieDetailsState.Loading)
    val state: StateFlow<MovieDetailsState> = _state.asStateFlow()

    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage.asStateFlow()

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    init {
        loadDetails()
    }

    fun loadDetails() {
        viewModelScope.launch {
            _state.value = MovieDetailsState.Loading
            try {
                val (details, source) = getMovieDetails(movieId)
                _state.value = MovieDetailsState.Success(
                    MovieDetailsResult(
                        details = details,
                        source = source
                    )
                )
                _uiMessage.value = "Данные получены из: ${source}"
            } catch (e: ApiException) {
                handleApiError(e)
            } catch (e: IOException) {
                handleNetworkError(e)
            } catch (e: Exception) {
                handleGenericError(e)
            }
        }
    }

    private fun handleApiError(e: ApiException) {
        val message = e.message
        _state.value = MovieDetailsState.Error(message)
        _uiMessage.value = message
    }

    private fun handleNetworkError(e: IOException) {
        val message = "Ошибка сети: ${e.message ?: "Проверьте подключение"}"
        _state.value = MovieDetailsState.Error(message)
        _uiMessage.value = message
    }

    private fun handleGenericError(e: Exception) {
        val message = e.message ?: "Неизвестная ошибка"
        _state.value = MovieDetailsState.Error(message)
        _uiMessage.value = message
    }

    fun clearMessage() {
        _uiMessage.value = null
    }

}

sealed class MovieDetailsState {
    object Loading : MovieDetailsState()
    data class Success(val result: MovieDetailsResult) : MovieDetailsState()
    data class Error(val message: String) : MovieDetailsState()
}

data class MovieDetailsResult(
    val details: MovieDetails,
    val source: Source
)