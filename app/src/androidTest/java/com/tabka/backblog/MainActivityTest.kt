package com.tabka.backblog

import android.content.Context
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{
    @Test
    fun test_isActivityInView() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.container)).check(matches(isDisplayed()))
    }

    @Test
    fun test_navToHomePage() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.navigation_home)).perform(click())
        onView(withId(R.id.fragment_home_parent)).check(matches(isDisplayed()))

        // Verify title is binded
        val title = getResourceString(R.string.title_home)
        onView(withId(R.id.toolbar_title)).check(matches(withText(title)))
    }

    @Test
    fun test_navToSearchPage() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.navigation_search)).perform(click())
        onView(withId(R.id.fragment_search_parent)).check(matches(isDisplayed()))

        // Verify title is binded
        val title = getResourceString(R.string.title_search)
        onView(withId(R.id.toolbar_title)).check(matches(withText(title)))
    }

    @Test
    fun test_navToFriendsPage() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.navigation_friends)).perform(click())
        onView(withId(R.id.fragment_friends_parent)).check(matches(isDisplayed()))

        // Verify title is binded
        val title = getResourceString(R.string.title_friends)
        onView(withId(R.id.toolbar_title)).check(matches(withText(title)))
    }

    private fun getResourceString(id: Int): String? {
        val targetContext: Context = InstrumentationRegistry.getTargetContext()
        return targetContext.getResources().getString(id)
    }
}