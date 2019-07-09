package com.zotikos.m4u.base

import com.zotikos.m4u.data.remote.ApiService
import com.zotikos.m4u.data.repository.MovieRepository
import com.zotikos.m4u.di.module.NetworkModule
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

open class BaseMockServerTest {

    val mockWebServer = MockWebServer()

    lateinit var mockApiService: ApiService

    lateinit var movieRepository: MovieRepository

    @Before
    @Throws(Exception::class)
    fun setup() {

        val networkModule = NetworkModule()
        mockWebServer.start()

        val serverUrl = mockWebServer.url("/").toString()
        val moshi = networkModule.provideMoshi()
        val httpClient = networkModule.provideOkHttpClient()
        val retrofit = networkModule.provideRetrofit(serverUrl, moshi, httpClient)

        mockApiService = retrofit.create(ApiService::class.java)
        movieRepository = MovieRepository(mockApiService)

    }


    @After
    @Throws(Exception::class)
    fun tearDown() {
        mockWebServer.shutdown()
    }
}