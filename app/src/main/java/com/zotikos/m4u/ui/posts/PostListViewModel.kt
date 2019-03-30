package com.zotikos.m4u.ui.post

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.zotikos.m4u.data.repository.PostRepository
import com.zotikos.m4u.ui.base.BaseViewModel
import com.zotikos.m4u.util.SchedulerProvider
import io.reactivex.disposables.Disposable

class PostListViewModel(
     val repository: PostRepository,
     val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private lateinit var subscription: Disposable

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    init {
        loadPosts()
    }

    private fun loadPosts() {
        subscription = repository.getPosts()
            .compose(schedulerProvider.getSchedulersForObservable())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                { onRetrievePostListSuccess() },
                { onRetrievePostListError() }
            )
    }


    private fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess() {

    }

    private fun onRetrievePostListError() {

    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}