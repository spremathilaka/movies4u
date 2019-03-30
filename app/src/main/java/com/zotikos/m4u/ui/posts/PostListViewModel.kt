package com.zotikos.m4u.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zotikos.m4u.data.repository.PostRepository
import com.zotikos.m4u.ui.base.BaseViewModel
import com.zotikos.m4u.ui.vo.PostUIDto
import com.zotikos.m4u.ui.vo.Resource
import com.zotikos.m4u.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class PostListViewModel(
    private val repository: PostRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {


    private val disposable by lazy { CompositeDisposable() }

    private val _dataList = MutableLiveData<Resource<List<PostUIDto>>>()


    //  private val _repoLoadError = MutableLiveData<Boolean>()
    // val errorMessage: MutableLiveData<Int> = MutableLiveData()
    //  private val _loading = MutableLiveData<Boolean>()

    fun loadPosts() {
        disposable.add(repository.getPosts()
            .compose(schedulerProvider.getSchedulersForObservable()).map { postList ->
                postList.map { PostUIDto(it) }
            }
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                // Add result
                { result -> onRetrievePostListSuccess(result) },
                { onRetrievePostListError() }

            ))
    }

    fun getPosts(): LiveData<Resource<List<PostUIDto>>> {
        return _dataList
    }


/*    fun getError(): LiveData<Boolean> {
        return _repoLoadError
    }

    fun getLoadingVisibility(): LiveData<Boolean> {
        return _loading
    }*/

    private fun onRetrievePostListStart() {
        _dataList.value = Resource.loading(mutableListOf())
        // errorMessage.value = null
    }

    private fun onRetrievePostListFinish() {
        //_loading.value = false
    }

    private fun onRetrievePostListSuccess(postList: List<PostUIDto>) {
        /// _repoLoadError.value = false
        // _loading.value = false
        _dataList.value = Resource.success(postList)
    }

    private fun onRetrievePostListError() {
        //errorMessage.value = com.zotikos.m4u.R.string.post_error
        //  _repoLoadError.value = true
        _dataList.value = Resource.error("error", mutableListOf())
    }

    override fun onCleared() {
        disposable.clear()
        disposable.dispose()
        super.onCleared()
    }
}