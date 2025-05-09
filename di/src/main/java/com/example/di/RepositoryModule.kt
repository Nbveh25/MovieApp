package com.example.di

import com.example.data.remote.api.MovieApi
import com.example.data.repository.MovieRepositoryImpl
import com.example.domain.repository.MovieRepository
import dagger.Binds
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
            api = movieApi
        )
    }


}