package com.zotikos.m4u.ui.main

import com.zotikos.m4u.ui.popularmovies.detail.MovieDetailFragment
import com.zotikos.m4u.ui.popularmovies.list.MovieListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class MainFragmentModule {

    @ContributesAndroidInjector
    internal abstract fun provideListFragment(): MovieListFragment

    @ContributesAndroidInjector
    internal abstract fun provideDetailsFragment(): MovieDetailFragment
}