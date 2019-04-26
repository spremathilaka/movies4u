package com.zotikos.m4u.ui.main

import com.zotikos.m4u.ui.post.detail.PostDetailFragment
import com.zotikos.m4u.ui.post.list.PostListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class MainFragmentModule {

    @ContributesAndroidInjector
    internal abstract fun provideListFragment(): PostListFragment

    @ContributesAndroidInjector
    internal abstract fun provideDetailsFragment(): PostDetailFragment
}