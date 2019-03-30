package com.zotikos.m4u.ui.dto

import android.os.Parcelable
import com.zotikos.m4u.data.model.Post
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostUIDto(val userId: Int, val id: Int, val title: String, val body: String) : Parcelable {
    constructor(post: Post) : this(post.userId, post.id, post.title, post.body)
}