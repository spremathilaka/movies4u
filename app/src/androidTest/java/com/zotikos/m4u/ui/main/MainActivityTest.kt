package com.zotikos.m4u.ui.main


import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.zotikos.m4u.R
import com.zotikos.m4u.UiTestApp
import com.zotikos.m4u.util.CustomMatchers.Companion.withItemCount
import com.zotikos.m4u.util.MockServerDispatcher
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, false)


    lateinit var mockWebServer: MockWebServer

    private lateinit var app: UiTestApp

    @Before
    @Throws(Exception::class)
    fun setup() {

        val instrumentation = InstrumentationRegistry.getInstrumentation()
        app = instrumentation.targetContext.applicationContext as UiTestApp

        val appInjector = DaggerUITestAppComponent.builder()
            .application(app)
            .build()
        appInjector.inject(app)

        mockWebServer = appInjector.getMockWebServer()


        val intent = Intent(
            InstrumentationRegistry.getInstrumentation()
                .targetContext, MainActivity::class.java
        )
        mockWebServer.setDispatcher(MockServerDispatcher().RequestDispatcher())
        activityRule.launchActivity(intent)
    }


    @Test
    fun testHappyCondition() {

        Espresso.onView(withId(com.zotikos.m4u.R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.postList)).check(matches(withItemCount(3)))
        Espresso.onView(withId(R.id.postList)).check(matches(isDisplayed()))

    }

/*    @Test
    fun recycleView_shouldHandleMalformedResponse() {
        mockWebServer.enqueue(MockResponse().setResponseCode(500).setBody("Error"))
        onView(withId(R.id.searchView)).perform(typeSearchViewText("test"))
        SystemClock.sleep(1000)
        onView(allOf(withId(R.id.snackbar_text), withText(app.getString(R.string.network_connection_error))))
            .check(matches(isDisplayed()))
    }*/

    @After
    @Throws(Exception::class)
    fun tearDown() {
        mockWebServer.shutdown()
    }
}