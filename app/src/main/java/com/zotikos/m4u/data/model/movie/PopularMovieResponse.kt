package com.zotikos.m4u.data.model.movie

data class PopularMovieResponse(
    val page: Int,
    val total_results: Int,
    val total_pages: Int,
    val results: List<Movie>
)