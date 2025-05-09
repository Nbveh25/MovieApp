package com.example.di

import com.example.domain.repository.MovieRepository
import com.example.domain.usecase.GetMovieDetailsUseCase
import com.example.domain.usecase.GetPopularMoviesUseCase
import com.example.domain.usecase.SearchMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetPopularMoviesUseCase(
        repository: MovieRepository
    ): GetPopularMoviesUseCase = GetPopularMoviesUseCase(repository)

    @Provides
    fun provideGetMovieDetailsUseCase(
        repository: MovieRepository
    ): GetMovieDetailsUseCase = GetMovieDetailsUseCase(repository)

    @Provides
    fun provideSearchMoviesUseCase(
        repository: MovieRepository
    ): SearchMoviesUseCase = SearchMoviesUseCase(repository)

}