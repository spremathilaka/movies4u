package com.zotikos.m4u.ui.post.list

import com.zotikos.m4u.ui.vo.PostUIDto


sealed class PostsListAction {
    class PostsLoadingSuccess(val newPosts: List<PostUIDto>, val oldPosts: List<PostUIDto>) : PostsListAction()
}