package com.zotikos.m4u.util

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest


class MockServerDispatcher {

    /**
     * Return ok response from mock server
     */
    internal inner class RequestDispatcher : Dispatcher() {


        override fun dispatch(request: RecordedRequest?): MockResponse {
            return when {
                request?.path == "/posts" -> MockResponse().setResponseCode(200).setBody(TestUtils.readJsonFile("json/post_response.json"))
                else -> MockResponse().setResponseCode(404)
            }

        }
    }

    /**
     * Return error response from mock server
     */
    internal inner class ErrorDispatcher : Dispatcher() {

        override fun dispatch(request: RecordedRequest): MockResponse {

            return MockResponse().setResponseCode(400)
        }
    }


}