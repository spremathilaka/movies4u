package com.zotikos.m4u.ui.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {


    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {

    }
}
