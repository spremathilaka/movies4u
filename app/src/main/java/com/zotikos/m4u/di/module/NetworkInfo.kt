package com.zotikos.m4u.di.module

import android.content.Context
import android.net.ConnectivityManager

class NetworkInfo(private val context: Context) {
    fun isNetworkAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }
}