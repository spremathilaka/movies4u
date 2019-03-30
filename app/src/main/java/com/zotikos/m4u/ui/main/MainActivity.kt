package com.zotikos.m4u.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zotikos.m4u.R
import com.zotikos.m4u.ui.base.BaseActivity
import com.zotikos.m4u.ui.posts.PostListFragment
import com.zotikos.m4u.ui.posts.PostListFragment.Companion.FRAGMENT_TAG
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), HasSupportFragmentInjector {



    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Fragment>

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.transaction(allowStateLoss = true) {
                    replace(R.id.content_frame, PostListFragment.newInstance(), FRAGMENT_TAG)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigation.selectedItemId = R.id.navigation_home;
    }

    override fun layoutRes(): Int = R.layout.activity_main

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return androidInjector
    }
}
