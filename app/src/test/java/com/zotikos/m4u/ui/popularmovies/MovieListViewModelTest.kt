package com.zotikos.m4u.ui.popularmovies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.zotikos.m4u.data.repository.MovieRepository
import com.zotikos.m4u.ui.base.CommonViewAction
import com.zotikos.m4u.ui.base.Event
import com.zotikos.m4u.ui.popularmovies.list.MovieListAction
import com.zotikos.m4u.ui.popularmovies.list.MovieListViewModel
import com.zotikos.m4u.util.SchedulerProvider
import com.zotikos.m4u.util.getDummyPostList
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.IOException

class MovieListViewModelTest {


    //Add an instant task executor rule to your unit tests to make live data emit immediately
    // rather than attempting to deal with handlers, loopers, etc.
    //A JUnit Test Rule that swaps the background executor used by the Architecture Components with a
    // different one which executes each task synchronously.

    @Rule
    @JvmField
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRepository: MovieRepository

    //LiveData will emit values is if it has an observer.
    // But we want to run our tests without creating an Activity or Fragment.
    @Mock
    lateinit var observer: Observer<Event<MovieListAction>>

    private val schedulerProvider = SchedulerProvider(
        Schedulers.trampoline(),
        Schedulers.trampoline()
    )

    private lateinit var postListViewModel: MovieListViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        postListViewModel = MovieListViewModel(mockRepository, schedulerProvider, "https://jsonlint.com/")
    }


    @Test
    fun should_call_success_event_when_api_call_success() {
        //If our live data does not have an observer, then onChanged events will not be emitted
        postListViewModel.getPosts().observeForever(observer)

        Mockito.`when`(mockRepository.getPopularMovies())
            .thenReturn(Single.just(getDummyPostList()))

        postListViewModel.loadPosts()

        val actual = postListViewModel.getPosts().value?.getContentIfNotHandled()

        Assert.assertTrue(actual is MovieListAction.MovieLoadingSuccessNew)
        assertEquals(3, (actual as MovieListAction.MovieLoadingSuccessNew).newMovies.size)
    }


    @Test
    fun should_callFailed_event_when_api_call_failed() {
        //If our live data does not have an observer, then onChanged events will not be emitted
        postListViewModel.getPosts().observeForever(observer)

        Mockito.`when`(mockRepository.getPopularMovies())
            .thenReturn(Single.error(IOException("")))

        postListViewModel.loadPosts()

        assert(
            postListViewModel.commonViewActionEvent.value?.getContentIfNotHandled()
                    is CommonViewAction.NonApplicationError
        )
        // assertEquals(0, (actual as MovieListAction.PostsLoadingSuccess).posts.size)
    }

}