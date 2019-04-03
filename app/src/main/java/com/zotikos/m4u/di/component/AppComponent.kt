package com.zotikos.m4u.di.component

import android.app.Application
import com.zotikos.m4u.M4UApp
import com.zotikos.m4u.di.module.*
import com.zotikos.m4u.di.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        ContextModule::class,
        AppModule::class,
        NetworkModule::class,
        UrlModule::class,
        ViewModelModule::class,
        ActivityBuilderModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        //fun networkModule(networkModule: NetworkModule): Builder

        @BindsInstance
        fun application(application: Application): Builder
    }

    fun inject(m4UApp: M4UApp)

}