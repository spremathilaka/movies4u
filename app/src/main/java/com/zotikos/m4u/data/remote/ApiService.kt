package com.zotikos.m4u.data.remote

import com.zotikos.m4u.data.model.Post
import io.reactivex.Observable

import retrofit2.http.GET

interface ApiService {

    @GET("/posts")
    fun getPosts(): Observable<List<Post>>
}