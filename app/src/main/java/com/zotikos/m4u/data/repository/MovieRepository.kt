package com.zotikos.m4u.data.repository

import com.zotikos.m4u.data.model.movie.PopularMovieResponse
import com.zotikos.m4u.data.remote.ApiService
import io.reactivex.Single
import javax.inject.Inject

class MovieRepository @Inject constructor(private val apiService: ApiService) {

    fun getPopularMovies(): Single<PopularMovieResponse> {
        return apiService.getPopularMovies()
    }
}