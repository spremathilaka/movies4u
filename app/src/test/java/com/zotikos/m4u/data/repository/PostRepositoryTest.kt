package com.zotikos.m4u.data.repository

import com.zotikos.m4u.data.model.Post
import com.zotikos.m4u.data.remote.ApiService
import com.zotikos.m4u.util.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PostRepositoryTest {

    @Mock
    private lateinit var mockApiService: ApiService

    private val schedulerProvider = SchedulerProvider(
        Schedulers.trampoline(),
        Schedulers.trampoline()
    )

    private lateinit var postRepository: PostRepository


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        postRepository = PostRepository(mockApiService)
    }

    @Test
    fun getPosts() {

        Mockito.`when`(mockApiService.getPosts())
            .thenReturn(Observable.just(getDummyPostList()))

        val testObserver = TestObserver<List<Post>>()

        postRepository.getPosts()
            .subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValue { postList -> postList.size == 1 }
    }

    private fun getDummyPostList(): List<Post> =
        mutableListOf(Post(1, 2, "Check unit Test", "Process finished with exit code 0"))
}