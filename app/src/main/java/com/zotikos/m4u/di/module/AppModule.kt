package com.zotikos.m4u.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.zotikos.m4u.util.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton


@Module
class AppModule {

    private val commonDataStorePref = "common_pref"

    @Provides
    @Singleton
    fun providePreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(
            commonDataStorePref,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideSchedulerProvider() = SchedulerProvider(Schedulers.io(), AndroidSchedulers.mainThread())
}

