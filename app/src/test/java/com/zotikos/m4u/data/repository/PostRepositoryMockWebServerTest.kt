package com.zotikos.m4u.data.repository

import com.zotikos.m4u.base.BaseMockServerTest
import com.zotikos.m4u.data.model.post.Post
import com.zotikos.m4u.util.TestUtils
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit

class PostRepositoryMockWebServerTest : BaseMockServerTest() {


    /* private val schedulerProvider = SchedulerProvider(
         Schedulers.trampoline(),
         Schedulers.trampoline()
     )*/

    @Test
    fun should_return_posts_when_api_success() {

        val path = "/posts"

        // Mock a response with status 200 and sample JSON output
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(TestUtils.readJsonFile("json/post_response.json"))

        // Enqueue request
        mockWebServer.enqueue(mockResponse)

        val testObserver = TestObserver<List<Post>>()
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        postRepository.getPosts()
            //.compose(schedulerProvider.getSchedulersForSingle())
            .subscribe(testObserver)

        testObserver.assertNoErrors()
        // testObserver.assertValueCount(1)
        testObserver.assertValue { postList -> postList.size == 3 }

        // Get the request that was just made
        val request = mockWebServer.takeRequest()
        // Make sure we made the request to the required path
        assertEquals(path, request.path)
    }


    @Test
    fun testPostsReturnsError() {
        val testObserver = TestObserver<List<Post>>()

        val path = "/posts"

        // Mock a response with status 200 and sample JSON output
        val mockResponse = MockResponse()
            .setResponseCode(500) // Simulate a 500 HTTP Code

        // Enqueue request
        mockWebServer.enqueue(mockResponse)

        // Call the API
        postRepository.getPosts()
            //.compose(schedulerProvider.getSchedulersForSingle())
            .subscribe(testObserver)
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        // No values
        testObserver.assertNoValues()
        // One error recorded
        assertEquals(1, testObserver.errorCount())

        // Get the request that was just made
        val request = mockWebServer.takeRequest()
        // Make sure we made the request to the required path
        assertEquals(path, request.path)

    }


    @Test
    fun testPostsTimeOutReturnsError() {
        val testObserver = TestObserver<List<Post>>()

        val path = "/posts"

        // Mock a response with status 200 and sample JSON output
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .throttleBody(104, 1, TimeUnit.SECONDS) // Simulate SocketTimeout
            .setBody(TestUtils.readJsonFile("json/post_response.json"))

        // Enqueue request
        mockWebServer.enqueue(mockResponse)

        // Call the API
        postRepository.getPosts()
            // .compose(schedulerProvider.getSchedulersForSingle())
            .subscribe(testObserver)
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        // No values
        //testObserver.assertNoValues()
        // One error recorded
        assertEquals(1, testObserver.errorCount())

        // Get the request that was just made
        val request = mockWebServer.takeRequest()
        // Make sure we made the request to the required path
        assertEquals(path, request.path)

    }
}