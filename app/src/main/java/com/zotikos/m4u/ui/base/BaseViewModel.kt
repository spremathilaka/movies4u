package com.zotikos.m4u.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()
    val commonViewActionEvent = MutableLiveData<Event<CommonViewAction>>()

    val showLoadingIndicator = MutableLiveData<Boolean>().apply { postValue(false) }
    val loadingIndicator: LiveData<Boolean>
        get() = showLoadingIndicator

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun showLoadingIndicator() {
        showLoadingIndicator.postValue(true)
    }

    fun hideLoadingIndicator() {
        showLoadingIndicator.postValue(false)
    }
}
