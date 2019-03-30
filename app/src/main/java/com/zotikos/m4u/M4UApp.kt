package com.zotikos.m4u

import android.app.Activity
import android.app.Application
import com.zotikos.m4u.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class M4UApp : Application(), HasActivityInjector
//, HasSupportFragmentInjector
{


    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    /* @Inject
     lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>*/

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    //   override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}
