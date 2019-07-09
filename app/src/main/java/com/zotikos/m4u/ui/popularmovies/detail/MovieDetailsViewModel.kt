package com.zotikos.m4u.ui.popularmovies.detail

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.zotikos.m4u.data.repository.MovieRepository
import com.zotikos.m4u.ui.base.BaseViewModel
import com.zotikos.m4u.ui.popularmovies.dto.MovieUIDto
import com.zotikos.m4u.util.SchedulerProvider

class MovieDetailsViewModel(
    private val repository: MovieRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {


    private val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    var movieItem: MovieUIDto? = null

    init {
        //  loadPostDetails()
    }

    private fun loadPostDetails() {
        compositeDisposable.add(
            repository.getPopularMovies()
            .compose(schedulerProvider.getSchedulersForSingle())
            .doOnSubscribe { onRetrievePostDetailsStart() }
            .doFinally { onRetrievePostDetailsFinish() }
            .subscribe(
                { onRetrievePostDetailsSuccess() },
                { onRetrievePostListError() }
            ))
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


}