package com.zotikos.m4u.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


    fun loadPosts() {

        disposable.add(repository.getPosts()
            .compose(schedulerProvider.getSchedulersForSingle()).map { postList ->
                postList.map { PostUIDto(it) }
            }
            .doOnSubscribe {
                showLoadingIndicator()
            }
            .doFinally {
                hideLoadingIndicator()
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
}