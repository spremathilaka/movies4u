package com.zotikos.m4u


class UiTestApp : M4UApp() {

    override fun initDaggerAppComponent() {
        DaggerUITestAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }
}