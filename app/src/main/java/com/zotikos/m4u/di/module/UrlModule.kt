package com.zotikos.m4u.di.module

import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class UrlModule {

    private val baseUrl: String = "https://api.themoviedb.org"
    private val baseImageUrl: String = "https://image.tmdb.org/t/p/w342"

    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaseUrl(): String {
        return baseUrl
    }

    @Provides
    @Singleton
    @Named("baseImageUrl")
    fun provideBaseImageUrl(): String {
        return baseImageUrl
    }
}