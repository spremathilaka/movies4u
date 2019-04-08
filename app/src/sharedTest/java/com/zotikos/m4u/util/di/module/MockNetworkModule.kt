package com.zotikos.m4u.util.di.module

import com.zotikos.m4u.di.module.NetworkModule
import dagger.Module

@Module
class MockNetworkModule : NetworkModule() {

    override val READ_TIMEOUT = 2.toLong()
    override val CONNECT_TIMEOUT = 2.toLong()
    override val WRITE_TIMEOUT = 2.toLong()


}