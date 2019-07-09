package com.zotikos.m4u.ui.main


import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.zotikos.m4u.UiTestApp
import com.zotikos.m4u.di.component.DaggerUITestAppComponent
import com.zotikos.m4u.util.CustomMatchers.Companion.withItemCount
import com.zotikos.m4u.util.MockServerDispatcher
import com.zotikos.m4u.util.TestUtils.checkSnackBarDisplayedByMessage
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    lateinit var mockWebServer: MockWebServer

    private lateinit var app: UiTestApp

    // private var mIdlingResource: IdlingResource? = null

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


    }


    /* @Before
     fun registerIdlingResource() {
         val activityScenario = ActivityScenario.launch(MainActivity::class.java)
         activityScenario.onActivity { activity ->
             mIdlingResource = activity.getIdlingResource()
             // To prove that the test fails, omit this call:
             IdlingRegistry.getInstance().register(mIdlingResource)
         }
     }*/


    @Test
    fun testHappyCondition() {
        mockWebServer.setDispatcher(MockServerDispatcher().RequestDispatcher())


        launchActivity()
        Espresso.onView(withId(com.zotikos.m4u.R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(com.zotikos.m4u.R.id.postList)).check(matches(withItemCount(3)))
        Espresso.onView(withId(com.zotikos.m4u.R.id.postList)).check(matches(isDisplayed()))

    }

    private fun launchActivity() {
        val intent = Intent(
            InstrumentationRegistry.getInstrumentation()
                .targetContext, MainActivity::class.java
        )
        activityRule.launchActivity(intent)
        // ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun should_show_unhandled_error_pop_up_for_api_error_response() {
        mockWebServer.enqueue(MockResponse().setResponseCode(500).setBody("Error"))
        launchActivity()
        onView(withText(app.getString(com.zotikos.m4u.R.string.error_unhandled))).check(matches(isDisplayed()))
    }


   /* @Test
    fun should_show_detail_view_when_click_on_item() {
        mockWebServer.setDispatcher(MockServerDispatcher().RequestDispatcher())
        launchActivity()
        onView(withId(com.zotikos.m4u.R.id.postList))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<MovieListAdapter.>(0, click())
            )
        onView(withText("sunt aut facere repellat provident occaecati excepturi optio reprehenderit")).check(
            matches(
                isDisplayed()
            )
        )
    }*/

    @Test
    fun should_show_snack_bar_message_when_no_internet() {
        val response = MockResponse()
        response.setBody("\"message\":\"Error\"").throttleBody(1, 2, TimeUnit.SECONDS)

        mockWebServer.enqueue(response)
        launchActivity()
        checkSnackBarDisplayedByMessage(com.zotikos.m4u.R.string.no_network)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        mockWebServer.shutdown()
    }


    /*  // Unregister your Idling Resource so it can be garbage collected and does not leak any memory
      @After
      fun unregisterIdlingResource() {
          if (mIdlingResource != null) {
              IdlingRegistry.getInstance().unregister(mIdlingResource)
          }
      }*/
}