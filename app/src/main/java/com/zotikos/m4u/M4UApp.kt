package com.zotikos.m4u

import android.app.Activity
import android.app.Application
import com.zotikos.m4u.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

open class M4UApp : Application(), HasActivityInjector {


    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        initDaggerAppComponent()
    }


    open fun initDaggerAppComponent() {
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }


    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}
