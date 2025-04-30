package com.example.homework.di

import com.example.homework.data.mapper.MovieMapper
import com.example.homework.data.remote.api.MovieApi
import com.example.homework.data.repository.MovieRepositoryImpl
import com.example.homework.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieApi: MovieApi,
    ): MovieRepository {
        return MovieRepositoryImpl(
            api = movieApi,
        )
    }
}