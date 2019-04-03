package com.zotikos.m4u.data.repository

import com.zotikos.m4u.data.model.post.Post
import com.zotikos.m4u.data.remote.ApiService
import io.reactivex.Single
import javax.inject.Inject

class PostRepository @Inject constructor(private val apiService: ApiService) {

    fun getPosts(): Single<List<Post>> {
        return apiService.getPosts()
    }
}