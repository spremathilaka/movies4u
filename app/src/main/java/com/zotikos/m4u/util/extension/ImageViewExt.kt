package com.zotikos.m4u.util.extension

import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.zotikos.m4u.R

fun ImageView.load(imageUrl: String) {
    Picasso.get()
        .load(imageUrl)
        .error(this.context.getDrawable(R.drawable.default_image)!!)
        .into(this)
}

fun ImageView.load(imageUrl: String?, callback: ImageLoadingCallback) {
    Picasso.get()
        .load(imageUrl)
        .into(this, object : Callback {
            override fun onSuccess() = callback.onLoadingSuccess()

            override fun onError(e: Exception?) = callback.onError(e)
        })
}

interface ImageLoadingCallback {
    fun onLoadingSuccess()
    fun onError(e: Exception?)
}