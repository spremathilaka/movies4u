package com.zotikos.m4u.util

import com.zotikos.m4u.data.model.movie.PopularMovieResponse

fun getDummyPostList(): PopularMovieResponse {
    val response = TestUtils.loadData("json/movie_response.json", PopularMovieResponse::class.java)
    return response!!
}