package com.zotikos.m4u.data.remote

import com.zotikos.m4u.data.model.post.Post
import io.reactivex.Single
import retrofit2.http.GET

interface ApiService {

    @GET("/posts")
    fun getPosts(): Single<List<Post>>
}