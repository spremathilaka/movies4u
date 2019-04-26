package com.zotikos.m4u

import android.app.Activity
import android.app.Application
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
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

        setUpPicasso()
        initDaggerAppComponent()
    }


    open fun initDaggerAppComponent() {
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }

    private fun setUpPicasso() {
        val picassoBuilder = Picasso.Builder(this)
        picassoBuilder.downloader(OkHttp3Downloader(this, Integer.MAX_VALUE.toLong()))
        val built = picassoBuilder.build()
        built.setIndicatorsEnabled(true)
        built.isLoggingEnabled = BuildConfig.DEBUG
        Picasso.setSingletonInstance(built)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}
