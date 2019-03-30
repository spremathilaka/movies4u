package com.zotikos.m4u.ui.posts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.zotikos.m4u.data.model.Post
import com.zotikos.m4u.data.repository.PostRepository
import com.zotikos.m4u.ui.vo.PostUIDto
import com.zotikos.m4u.ui.vo.Resource
import com.zotikos.m4u.ui.vo.Status
import com.zotikos.m4u.util.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class PostListViewModelTest {


    //Add an instant task executor rule to your unit tests to make live data emit immediately
    // rather than attempting to deal with handlers, loopers, etc.
    //A JUnit Test Rule that swaps the background executor used by the Architecture Components with a
    // different one which executes each task synchronously.

    @Rule
    @JvmField
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRepository: PostRepository

    //LiveData will emit values is if it has an observer.
    // But we want to run our tests without creating an Activity or Fragment.
    @Mock
    lateinit var observer: Observer<Resource<List<PostUIDto>>>

    private val schedulerProvider = SchedulerProvider(
        Schedulers.trampoline(),
        Schedulers.trampoline()
    )

    private lateinit var postListViewModel: PostListViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        postListViewModel = PostListViewModel(mockRepository, schedulerProvider)
    }


    @Test
    fun should_call_loading_when_api_call_trigger() {


        postListViewModel.getPosts().observeForever(observer)

        Mockito.`when`(mockRepository.getPosts())
            .thenReturn(Observable.just(getDummyPostList()))

        postListViewModel.loadPosts()

        verify(observer).onChanged(Resource.loading(mutableListOf()))


    }

    @Test
    fun should_callSucess_event_when_api_call_success() {
        //If our livedata doesn’t have an observer, then onChanged events will not be emitted
        postListViewModel.getPosts().observeForever(observer)

        Mockito.`when`(mockRepository.getPosts())
            .thenReturn(Observable.just(getDummyPostList()))

        postListViewModel.loadPosts()

        assert(postListViewModel.getPosts().value?.status == Status.SUCCESS)
        assertEquals(1, postListViewModel.getPosts().value?.data?.size)
    }


    @Test
    fun should_callFailed_event_when_api_call_failed() {
        //If our livedata doesn’t have an observer, then onChanged events will not be emitted
        postListViewModel.getPosts().observeForever(observer)

        Mockito.`when`(mockRepository.getPosts())
            .thenReturn(Observable.error(Exception("")))

        postListViewModel.loadPosts()

        assert(postListViewModel.getPosts().value?.status == Status.ERROR)
        assertEquals(0, postListViewModel.getPosts().value?.data?.size)
    }

    private fun getDummyPostList(): List<Post> =
        mutableListOf(Post(1, 2, "Check unit Test", "Process finished with exit code 0"))
}