package com.zotikos.m4u.ui.main

import com.zotikos.m4u.ui.detail.PostDetailFragment
import com.zotikos.m4u.ui.posts.PostListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class MainFragmentModule {

    @ContributesAndroidInjector
    internal abstract fun provideListFragment(): PostListFragment

    @ContributesAndroidInjector
    internal abstract fun provideDetailsFragment(): PostDetailFragment
}