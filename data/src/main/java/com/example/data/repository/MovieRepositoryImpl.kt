package com.example.data.repository

import android.util.Log
import com.example.data.mapper.MovieMapper
import com.example.data.remote.api.MovieApi
import com.example.domain.enums.Source
import com.example.domain.exception.ApiException
import com.example.domain.model.Movie
import com.example.domain.model.MovieDetails
import com.example.domain.repository.MovieRepository
import retrofit2.HttpException
import java.io.IOException

class MovieRepositoryImpl(
    private val api: MovieApi,
) : MovieRepository {

    private val cache = mutableMapOf<Int, Pair<MovieDetails, Long>>()
    private val requestHistory = mutableListOf<Int>()
    private val CACHE_DURATION = 10 * 1000
    private val MAX_HISTORY_SIZE = 50


    override suspend fun getPopularMovies(): List<Movie> {
        try {
            val response = api.getPopularMovies()
            return response.results.map { MovieMapper.mapToDomain(it) }
        } catch (e: HttpException) {
            handleException(e)
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Pair<MovieDetails, Source> {
        return try {
            updateRequestHistory(movieId)

            if (shouldUseCache(movieId)) {
                val cachedData = cache[movieId]!!.first
                Pair(cachedData, Source.CACHE)
            } else {
                val response = api.getMovieDetails(movieId)
                val movieDetails = MovieMapper.mapToDomain(response)

                cache[movieId] = Pair(movieDetails, System.currentTimeMillis())
                Pair(movieDetails, Source.API)
            }
        } catch (e: Exception) {
            cache[movieId]?.let {
                if (System.currentTimeMillis() - it.second <= CACHE_DURATION) {
                    return Pair(it.first, Source.CACHE)
                }
            }
            handleException(e)
        }
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        try {
            val response = api.searchMovies(query)
            Log.d("MovieRepository", "response: $response")
            return response.results.map { MovieMapper.mapToDomain(it) }
        } catch (e: HttpException) {
            handleException(e)
        }
    }

    private fun updateRequestHistory(movieId: Int) {
        requestHistory.add(movieId)
        if (requestHistory.size > MAX_HISTORY_SIZE) {
            requestHistory.removeAt(0)
        }
    }

    private fun shouldUseCache(movieId: Int): Boolean {
        val cachedEntry = cache[movieId] ?: return false

        if (System.currentTimeMillis() - cachedEntry.second > CACHE_DURATION) {
            return false
        }

        val lastIndex = requestHistory.lastIndexOf(movieId)
        Log.d("MovieRepository", "size: ${requestHistory.size}")
        Log.d("MovieRepository", "lastIndex: $lastIndex")
        return if (lastIndex == -1) {
            false
        } else {
            val totalRequestsAfter = requestHistory.size - lastIndex - 1
            totalRequestsAfter < 3

        }
    }

    private fun handleException(e: Exception): Nothing {
        when (e) {
            is HttpException -> {
                val errorMessage = when (e.code()) {
                    400 -> "Неверный запрос"
                    401 -> "Ошибка авторизации"
                    404 -> "Фильм не найден"
                    else -> "Ошибка сервера: ${e.code()}"
                }
                throw ApiException(e.code(), errorMessage)
            }

            is IOException -> {
                Log.e("MovieRepository", "Ошибка сети", e)
                throw Exception("Ошибка сети: ${e.message}")
            }

            else -> {
                Log.e("MovieRepository", "Неизвестная ошибка", e)
                throw e
            }
        }
    }
}