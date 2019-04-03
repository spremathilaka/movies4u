package com.zotikos.m4u.util

import com.zotikos.m4u.data.model.post.Post

fun getDummyPostList(): List<Post> {
    val ret = Array<Post>::class.java
    return TestUtils.loadData("json/post_response.json", ret)!!.toList()
}