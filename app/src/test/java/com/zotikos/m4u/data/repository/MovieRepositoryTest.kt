package com.zotikos.m4u.data.repository

import com.zotikos.m4u.data.model.post.Post
import com.zotikos.m4u.data.remote.ApiService
import com.zotikos.m4u.util.SchedulerProvider
import com.zotikos.m4u.util.getDummyPostList
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MovieRepositoryTest {

    @Mock
    private lateinit var mockApiService: ApiService

    private val schedulerProvider = SchedulerProvider(
        Schedulers.trampoline(),
        Schedulers.trampoline()
    )

    private lateinit var movieRepository: MovieRepository


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        movieRepository = MovieRepository(mockApiService)
    }

    @Test
    fun should_return_posts_when_api_success() {

        Mockito.`when`(mockApiService.getPopularMovies())
            .thenReturn(Single.just(getDummyPostList()))

        val testObserver = TestObserver<List<Post>>()

        movieRepository.getPopularMovies()
            .subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValue { postList -> postList.size == 3 }
    }


}