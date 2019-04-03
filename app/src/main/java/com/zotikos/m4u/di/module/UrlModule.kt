package com.zotikos.m4u.di.module

import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class UrlModule {

    private val baseUrl: String = "https://jsonplaceholder.typicode.com"

    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaseUrl(): String {
        return baseUrl
    }
}