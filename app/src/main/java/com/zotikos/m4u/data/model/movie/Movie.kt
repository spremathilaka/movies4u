package com.zotikos.m4u.data.model.movie

data class Movie(
    val vote_count: Int,
    val id: Int,
    val video: Boolean,
    val vote_average: Float,
    val title: String,
    val popularity: Float,
    val poster_path: String,
    val genre_ids: List<Int>,
    val backdrop_path: String,
    val adult: Boolean,
    val overview: String,
    val release_date: String

)
