package com.zotikos.m4u.data.repository

import com.zotikos.m4u.data.model.Post
import com.zotikos.m4u.data.remote.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class PostRepository @Inject constructor(private val apiService: ApiService) {

    fun getPosts(): Observable<List<Post>> {
        return apiService.getPosts()
    }
}