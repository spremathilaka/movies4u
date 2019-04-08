package com.zotikos.m4u.di.component


import android.app.Application
import com.zotikos.m4u.M4UApp
import com.zotikos.m4u.data.remote.ApiService
import com.zotikos.m4u.di.module.ActivityBuilderModule
import com.zotikos.m4u.di.module.AppModule
import com.zotikos.m4u.di.module.ContextModule
import com.zotikos.m4u.di.module.ViewModelModule
import com.zotikos.m4u.util.di.module.MockNetworkModule
import com.zotikos.m4u.util.di.module.MockUrlModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton


@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        ContextModule::class,
        AppModule::class,
        MockUrlModule::class,
        MockNetworkModule::class,
        ViewModelModule::class,
        ActivityBuilderModule::class
    ]
)
interface UITestAppComponent {

    @Component.Builder
    interface Builder {
        fun build(): UITestAppComponent

        //fun networkModule(networkModule: NetworkModule): Builder

        @BindsInstance
        fun application(application: Application): Builder
    }

    fun getMockWebServer(): MockWebServer

    fun getApiService(): ApiService

    fun inject(m4UApp: M4UApp)

}