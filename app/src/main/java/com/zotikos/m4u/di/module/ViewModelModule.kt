package com.zotikos.m4u.di.module

import androidx.lifecycle.ViewModel
import com.zotikos.m4u.data.repository.PostRepository
import com.zotikos.m4u.ui.detail.PostDetailsViewModel
import com.zotikos.m4u.ui.main.MainActivityViewModel
import com.zotikos.m4u.ui.posts.PostListViewModel
import com.zotikos.m4u.util.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
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
    @ViewModelKey(PostListViewModel::class)
    fun providePostListViewModel(repository: PostRepository, schedulerProvider: SchedulerProvider): ViewModel {
        return PostListViewModel(repository, schedulerProvider)
    }

    @Provides
    @IntoMap
    @ViewModelKey(PostDetailsViewModel::class)
    fun providePostDetailsViewModel(repository: PostRepository, schedulerProvider: SchedulerProvider): ViewModel {
        return PostDetailsViewModel(repository, schedulerProvider)
    }


    @Provides
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    fun bindMainViewModel(): ViewModel {
        return MainActivityViewModel()
    }
    //Add more ViewModels here
}