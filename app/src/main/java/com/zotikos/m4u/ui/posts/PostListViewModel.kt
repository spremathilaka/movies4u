package com.zotikos.m4u.ui.posts

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.idling.CountingIdlingResource
import com.zotikos.m4u.REQUEST_API_GET_POSTS
import com.zotikos.m4u.data.repository.PostRepository
import com.zotikos.m4u.ui.base.ApiSingleDisposableObserver
import com.zotikos.m4u.ui.base.BaseViewModel
import com.zotikos.m4u.ui.vo.Event
import com.zotikos.m4u.ui.vo.PostUIDto
import com.zotikos.m4u.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class PostListViewModel(
    private val repository: PostRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {


    private val disposable by lazy { CompositeDisposable() }

    private val _dataList = MutableLiveData<Event<PostsListAction>>()

    var mIdlingRes = CountingIdlingResource("PostList")


    fun loadPosts() {

        disposable.add(repository.getPosts()
            .compose(schedulerProvider.getSchedulersForSingle()).map { postList ->
                postList.map { PostUIDto(it) }
            }
            .doOnSubscribe {
                showLoadingIndicator()
                mIdlingRes.increment()
            }
            .doFinally {
                hideLoadingIndicator()
                mIdlingRes.decrement()
            }
            .subscribeWith(GetPostsSingle()))
    }

    fun getPosts(): LiveData<Event<PostsListAction>> {
        return _dataList
    }

    private inner class GetPostsSingle : ApiSingleDisposableObserver<List<PostUIDto>>(
        REQUEST_API_GET_POSTS,
        commonViewActionEvent,
        showLoadingIndicator
    ) {

        override fun onSuccess(response: List<PostUIDto>) {
            super.onSuccess(response)
            _dataList.value = Event(PostsListAction.PostsLoadingSuccess(response))
        }
    }


    override fun onCleared() {
        disposable.clear()
        disposable.dispose()
        super.onCleared()
    }

    @VisibleForTesting
    fun getIdlingResourceInTest(): CountingIdlingResource {
        return mIdlingRes
    }
}