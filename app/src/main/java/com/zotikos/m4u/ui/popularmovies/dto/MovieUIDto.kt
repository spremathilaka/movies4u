package com.zotikos.m4u.ui.popularmovies.dto

import android.os.Parcelable
import com.zotikos.m4u.data.model.movie.Movie
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieUIDto(
    val id: Int,
    val title: String,
    val body: String,
    val imageUrl: String = "",
    val baseUrl: String = ""

) : Parcelable {

    constructor(post: Movie, baseUrl: String) : this(post.id, post.title, post.overview, post.poster_path, baseUrl)

    val posterImageURl: String
        get() {
            return baseUrl + imageUrl
        }
}