package com.zotikos.m4u.di.module

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.zotikos.m4u.API_KEY
import com.zotikos.m4u.BuildConfig
import com.zotikos.m4u.data.remote.ApiService
import com.zotikos.m4u.util.exceptions.OfflineException
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


/**
 * Module which provides all required dependencies about network
 */

// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")

private const val READ_TIMEOUT = 30.toLong()
private const val CONNECT_TIMEOUT = 30.toLong()
private const val WRITE_TIMEOUT = 30.toLong()
private const val CACHE_SIZE = 10 * 1024 * 1024.toLong() // 5 MB

@Module
class NetworkModule {


    companion object {
        private val TAG = NetworkModule::class.simpleName

        private const val OFFLINE_INTERCEPTOR = "offlineInterceptor"
        private const val DELAY_INTERCEPTOR = "delayInterceptor"
        private const val BASE_URL = "baseUrl"
    }

    @Provides
    @Singleton
    fun provideNetworkInfo(context: Context): NetworkInfo {
        return NetworkInfo(context)
    }

    @Provides
    @Singleton
    @Named(OFFLINE_INTERCEPTOR)
    fun provideOfflineCheckInterceptor(networkInfo: NetworkInfo): Interceptor {
        return Interceptor { chain ->
            if (networkInfo.isNetworkAvailable()) {
                chain.proceed(chain.request())
            } else {
                throw OfflineException()
            }
        }
    }

    private fun getInterceptorLevel(): HttpLoggingInterceptor.Level {
        return if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    fun provideHttpLoginInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val level = getInterceptorLevel()
        httpLoggingInterceptor.level = level
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    @Named(DELAY_INTERCEPTOR)
    fun provideDelayInterceptor(): Interceptor {
        return Interceptor { chain ->
            try {
                Thread.sleep(BuildConfig.NETWORK_DELAY)
            } catch (e: Exception) {
                Timber.e(Log.getStackTraceString(e))
            }

            try {
                chain.proceed(chain.request())
            } catch (e: Exception) {
                Timber.e(Log.getStackTraceString(e))
                throw e
            }
        }
    }


    /**
     * Provides the Post service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the Post service implementation.
     */
    @Provides
    @Reusable
    internal fun providePostApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */

    @Provides
    @Reusable
    internal fun provideRetrofit(@Named(BASE_URL) baseUrl: String, moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache {
        return Cache(application.cacheDir, CACHE_SIZE)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cache: Cache? = null,
        loggingInterceptor: HttpLoggingInterceptor? = null,
        @Named(DELAY_INTERCEPTOR) delayInterceptor: Interceptor? = null,
        @Named(OFFLINE_INTERCEPTOR) offlineCheckInterceptor: Interceptor? = null
    ): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)



        if (cache != null) okHttpBuilder.cache(cache)
        if (loggingInterceptor != null) okHttpBuilder.addInterceptor(loggingInterceptor)
        if (offlineCheckInterceptor != null) okHttpBuilder.addInterceptor(offlineCheckInterceptor)

        if (BuildConfig.DEBUG && delayInterceptor != null) {
            okHttpBuilder.addInterceptor(delayInterceptor)
        }

        if (BuildConfig.API_KEY.isNotEmpty()) {
            okHttpBuilder.addInterceptor {
                val request = it.request()
                val url = request.url().newBuilder()
                    .addQueryParameter(API_KEY, BuildConfig.API_KEY)
                    .build()
                it.proceed(request.newBuilder().url(url).build())
            }
        }

        return okHttpBuilder.build()
    }
}