package com.zotikos.m4u.ui.posts

import com.zotikos.m4u.ui.vo.PostUIDto


sealed class PostsListAction {
    class PostsLoadingSuccess(val posts: List<PostUIDto>) : PostsListAction()
}