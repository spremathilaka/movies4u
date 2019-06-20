package com.zotikos.m4u.ui.post.list

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

    private var newDataList = mutableListOf<PostUIDto>()

    //var oldDataList = mutableListOf<PostUIDto>()

    // var previousDiffResult: DiffUtil.DiffResult? = null


    /*fun loadPosts(isSwipeRefreshAction: Boolean = false) {

        if (newDataList.isEmpty() or isSwipeRefreshAction) {
            newDataList.clear()
            disposable.add(repository.getPosts()
                .compose(schedulerProvider.getSchedulersForSingle()).map { postList ->
                    postList.map { PostUIDto(it) }
                }
                .doOnSubscribe {
                    if (isSwipeRefreshAction.not()) {
                        showLoadingIndicator()
                    }
                }
                .doFinally {
                    if (isSwipeRefreshAction.not()) {
                        hideLoadingIndicator()
                    }
                }
                .subscribeWith(GetPostsSingle()))
        } else {
            _dataList.value = Event(PostsListAction.PostsLoadingSuccess(newDataList, oldDataList))
        }
    }*/

    fun loadPosts(isSwipeRefreshAction: Boolean = false) {

        if (newDataList.isEmpty() or isSwipeRefreshAction) {
            newDataList.clear()

            disposable.add(repository.getPosts()
                .compose(schedulerProvider.getSchedulersForSingle()).map { postList ->
                    postList.map { PostUIDto(it) }
                }

                /*.flatMap {
                    newDataList.addAll(it)
                    val diffResult = DiffUtil.calculateDiff(PostItemDiffCallback(oldDataList, newDataList),false)
                    previousDiffResult = diffResult
                    Single.just(Pair(diffResult, newDataList))
                }*/
                .doOnSubscribe {
                    if (isSwipeRefreshAction.not()) {
                        showLoadingIndicator()
                    }
                }
                .doFinally {
                    if (isSwipeRefreshAction.not()) {
                        hideLoadingIndicator()
                    }
                }
                .subscribeWith(GetPostsDiffResultSingle()))
        } else {
            _dataList.value = Event(PostsListAction.PostsLoadingSuccessNew(newDataList))
        }
    }


    fun getPosts(): LiveData<Event<PostsListAction>> {
        return _dataList
    }

/*    private inner class GetPostsSingle : ApiSingleDisposableObserver<List<PostUIDto>>(
        REQUEST_API_GET_POSTS,
        commonViewActionEvent,
        showLoadingIndicator,
        { loadPosts() }
    ) {

        override fun onSuccess(response: List<PostUIDto>) {
            super.onSuccess(response)
            newDataList.addAll(response)
            _dataList.value = Event(PostsListAction.PostsLoadingSuccess(response, oldDataList))
        }
    }*/

    private inner class GetPostsDiffResultSingle :
        ApiSingleDisposableObserver<List<PostUIDto>>(
            REQUEST_API_GET_POSTS,
            commonViewActionEvent,
            showLoadingIndicator,
            { loadPosts() }
        ) {
        override fun onSuccess(response: List<PostUIDto>) {
            super.onSuccess(response)
            _dataList.value = Event(PostsListAction.PostsLoadingSuccessNew(response))
            newDataList.clear()
            newDataList.addAll(response)
        }

    }


    override fun onCleared() {
        disposable.clear()
        disposable.dispose()
        super.onCleared()
    }
}