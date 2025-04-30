package com.example.homework.data.repository

import android.util.Log
import com.example.homework.data.exception.ApiException
import com.example.homework.data.mapper.MovieMapper
import com.example.homework.data.remote.api.MovieApi
import com.example.homework.domain.model.Movie
import com.example.homework.domain.model.MovieDetails
import com.example.homework.domain.repository.MovieRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi
) : MovieRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        try {
            val response = api.getPopularMovies()
            return response.results.map { MovieMapper.mapToDomain(it) }
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> throw ApiException(400, "Неверный запрос. Проверьте параметры запроса.")
                401 -> throw ApiException(401, "Ошибка авторизации. Проверьте API ключ.")
                404 -> throw ApiException(404, "Фильмы не найдены.")
                else -> throw ApiException(e.code(), "Ошибка сервера: ${e.code()}")
            }
        } catch (e: IOException) {
            Log.e("MovieRepository", "Ошибка сети при загрузке популярных фильмов", e)
            throw Exception("Ошибка сети: ${e.message}")
        } catch (e: Exception) {
            Log.e("MovieRepository", "Неизвестная ошибка при загрузке популярных фильмов", e)
            throw e
        }
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetails {
        try {
            val response = api.getMovieDetails(movieId)
            return MovieMapper.mapToDomain(response)
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> throw ApiException(400, "Неверный запрос. Проверьте параметры запроса.")
                401 -> throw ApiException(401, "Ошибка авторизации. Проверьте API ключ.")
                404 -> throw ApiException(404, "Фильм не найден.")
                else -> throw ApiException(e.code(), "Ошибка сервера: ${e.code()}")
            }
        } catch (e: IOException) {
            Log.e("MovieRepository", "Ошибка сети при загрузке деталей фильма", e)
            throw Exception("Ошибка сети: ${e.message}")
        } catch (e: Exception) {
            Log.e("MovieRepository", "Неизвестная ошибка при загрузке деталей фильма", e)
            throw e
        }
    }
}