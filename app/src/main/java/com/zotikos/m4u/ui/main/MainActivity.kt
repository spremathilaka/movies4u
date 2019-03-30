package com.zotikos.m4u.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.ViewModelProviders
import com.zotikos.m4u.R
import com.zotikos.m4u.di.module.ViewModelFactory
import com.zotikos.m4u.ui.base.BaseActivity
import com.zotikos.m4u.ui.detail.PostDetailFragment
import com.zotikos.m4u.ui.posts.PostListFragment
import com.zotikos.m4u.ui.vo.PostUIDto
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : BaseActivity(), HasSupportFragmentInjector,
    PostListFragment.OnFragmentInteractionListener,
    PostDetailFragment.OnFragmentInteractionListener {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var activityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        activityViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)
        loadNextView(PostListFragment.newInstance(), PostListFragment.FRAGMENT_TAG)
    }

    override fun layoutRes(): Int = R.layout.activity_main

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return androidInjector
    }

    private fun loadNextView(fragment: Fragment, fragmentTag: String, addToBackStack: Boolean = true) {
        supportFragmentManager.transaction(allowStateLoss = true) {
            replace(R.id.content_frame, fragment, fragmentTag)
            if (addToBackStack) {
                addToBackStack(fragmentTag)
            }
        }
    }

    override fun showPostDetail(post: PostUIDto) {
        loadNextView(PostDetailFragment.newInstance(post), PostDetailFragment.FRAGMENT_TAG, addToBackStack = false)
    }
}
