package com.zotikos.m4u.ui.popularmovies.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zotikos.m4u.REQUEST_API_GET_POSTS
import com.zotikos.m4u.data.repository.MovieRepository
import com.zotikos.m4u.ui.base.ApiSingleDisposableObserver
import com.zotikos.m4u.ui.base.BaseViewModel
import com.zotikos.m4u.ui.base.Event
import com.zotikos.m4u.ui.popularmovies.dto.MovieUIDto
import com.zotikos.m4u.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class MovieListViewModel(
    private val repository: MovieRepository,
    private val schedulerProvider: SchedulerProvider,
    private val baseUrl: String

) : BaseViewModel() {

    private val disposable by lazy { CompositeDisposable() }

    private val _dataList = MutableLiveData<Event<MovieListAction>>()

    private var newDataList = mutableListOf<MovieUIDto>()



    fun loadPosts(isSwipeRefreshAction: Boolean = false) {

        if (newDataList.isEmpty() or isSwipeRefreshAction) {
            newDataList.clear()

            disposable.add(
                repository.getPopularMovies()
                    .compose(schedulerProvider.getSchedulersForSingle())
                    .map { response ->
                        val mappedItems = mutableListOf<MovieUIDto>()
                        response.results.forEach {
                            mappedItems.add(MovieUIDto(it, baseUrl))
                        }
                        mappedItems

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
                .subscribeWith(GetPostsDiffResultSingle()))
        } else {
            _dataList.value = Event(MovieListAction.MovieLoadingSuccessNew(newDataList))
        }
    }


    fun getPosts(): LiveData<Event<MovieListAction>> {
        return _dataList
    }


    private inner class GetPostsDiffResultSingle :
        ApiSingleDisposableObserver<List<MovieUIDto>>(
            REQUEST_API_GET_POSTS,
            commonViewActionEvent,
            showLoadingIndicator,
            { loadPosts() }
        ) {
        override fun onSuccess(response: List<MovieUIDto>) {
            super.onSuccess(response)
            _dataList.value = Event(MovieListAction.MovieLoadingSuccessNew(response))
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