package com.tabka.backblog

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tabka.backblog.ui.Utilities.JsonUtility
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matcher
import org.hamcrest.number.OrderingComparison.greaterThan
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class HomeNavigationTest{

/*    val jsonData : String = "[{\"id\":\"fb773615-eac3-44ad-8b75-e1270373e7b8\",\"is_visible\":true,\"movie_ids\":[],\"name\":\"First Log\",\"priority\":1,\"watched_ids\":[]},{\"id\":\"57e7290e-6cd4-4016-b385-7f7ba52b3b9b\",\"is_visible\":true,\"movie_ids\":[],\"name\":\"Second Log\",\"priority\":2,\"watched_ids\":[]},{\"id\":\"cf088d56-9066-4cd6-b05c-f16373609f2f\",\"is_visible\":true,\"movie_ids\":[],\"name\":\"Third Log\",\"priority\":3,\"watched_ids\":[]},{\"id\":\"57e38f3e-fcfc-42d9-95b0-b528846a72a6\",\"is_visible\":true,\"movie_ids\":[],\"name\":\"Fourth Log\",\"priority\":4,\"watched_ids\":[]},{\"id\":\"cf06a096-5ea2-499f-b843-042e13d8b245\",\"is_visible\":true,\"movie_ids\":[],\"name\":\"Fifth Log\",\"priority\":5,\"watched_ids\":[]}];"
    val gson = Gson()
    val type = object : TypeToken<List<JsonUtility.UserLog>>() {}.type
    val list = Gson().fromJson<List<JsonUtility.UserLog>>(jsonData, type)*/

    @Test
    fun test_HomeFragmentNavigation() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // Nav Add Log Fragment
        onView(withId(R.id.button_add_log)).perform(click())
        onView(withId(R.id.popup_add_log_parent)).check(matches(isDisplayed()))

        // Click back
        pressBack()

        onView(withId(R.id.navigation_home)).check(matches(isDisplayed()))
    }
}
