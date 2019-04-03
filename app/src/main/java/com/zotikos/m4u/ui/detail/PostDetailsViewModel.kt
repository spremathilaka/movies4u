package com.zotikos.m4u.ui.detail

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.zotikos.m4u.data.repository.PostRepository
import com.zotikos.m4u.ui.base.BaseViewModel
import com.zotikos.m4u.ui.vo.PostUIDto
import com.zotikos.m4u.util.SchedulerProvider
import io.reactivex.disposables.Disposable

class PostDetailsViewModel(
    private val repository: PostRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private lateinit var subscription: Disposable

    private val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    var postItem: PostUIDto? = null

    init {
        loadPostDetails()
    }

    private fun loadPostDetails() {
        subscription = repository.getPosts()
            .compose(schedulerProvider.getSchedulersForSingle())
            .doOnSubscribe { onRetrievePostDetailsStart() }
            .doFinally { onRetrievePostDetailsFinish() }
            .subscribe(
                { onRetrievePostDetailsSuccess() },
                { onRetrievePostListError() }
            )
    }


    private fun onRetrievePostDetailsStart() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun onRetrievePostDetailsFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostDetailsSuccess() {

    }

    private fun onRetrievePostListError() {

    }

    override fun onCleared() {
        subscription.dispose()
        super.onCleared()
    }
}