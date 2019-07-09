package com.zotikos.m4u.data.remote

import com.zotikos.m4u.data.model.movie.PopularMovieResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ApiService {

    @GET("/3/movie/popular")
    fun getPopularMovies(): Single<PopularMovieResponse>
}