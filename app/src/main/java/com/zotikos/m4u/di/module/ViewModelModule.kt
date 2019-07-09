package com.zotikos.m4u.di.module

import androidx.lifecycle.ViewModel
import com.zotikos.m4u.data.repository.MovieRepository
import com.zotikos.m4u.ui.main.MainActivityViewModel
import com.zotikos.m4u.ui.popularmovies.detail.MovieDetailsViewModel
import com.zotikos.m4u.ui.popularmovies.list.MovieListViewModel
import com.zotikos.m4u.util.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named
import javax.inject.Provider

@Module
class ViewModelModule {


    //error: A @Module may not contain both non-static @Provides methods and abstract @Binds or @Multi binds declarations
    // @Binds
    // abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Provides
    fun providesViewModelFactory(viewModelMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelFactory {
        return ViewModelFactory(viewModelMap)
    }

    @Provides
    @IntoMap
    @ViewModelKey(MovieListViewModel::class)
    fun providePostListViewModel(
        repository: MovieRepository,
        schedulerProvider: SchedulerProvider
        , @Named("baseImageUrl") bserURl: String
    ): ViewModel {
        return MovieListViewModel(repository, schedulerProvider, bserURl)
    }

    @Provides
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    fun providePostDetailsViewModel(repository: MovieRepository, schedulerProvider: SchedulerProvider): ViewModel {
        return MovieDetailsViewModel(repository, schedulerProvider)
    }


    @Provides
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    fun bindMainViewModel(): ViewModel {
        return MainActivityViewModel()
    }
    //Add more ViewModels here
}