package com.zotikos.m4u.ui.vo

import android.os.Parcelable
import com.zotikos.m4u.data.model.post.Post
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostUIDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String,
    val imageUrl: String = "https://homepages.cae.wisc.edu/~ece533/images/girl.png"
) : Parcelable {
    constructor(post: Post) : this(post.userId, post.id, post.title, post.body)
}