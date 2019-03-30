package com.zotikos.m4u.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {


    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
           // is PostListViewModel -> injector.inject(this)
        }
    }
}
