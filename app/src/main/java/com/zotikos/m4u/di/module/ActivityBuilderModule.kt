package com.zotikos.m4u.di.module

import com.zotikos.m4u.ui.main.MainActivity
import com.zotikos.m4u.ui.main.MainFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    //we avoid writing @Subcomponent
    // code for each activity by using @ContributesAndroidInjector.
    @ContributesAndroidInjector(modules = [MainFragmentModule::class])
    abstract fun bindMainActivity(): MainActivity

}