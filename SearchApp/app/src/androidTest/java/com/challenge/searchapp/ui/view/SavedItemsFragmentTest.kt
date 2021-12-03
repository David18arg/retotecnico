package com.challenge.searchapp.ui.view


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.challenge.searchapp.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SavedItemsFragmentTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(ItemsActivity::class.java)

    @Test
    fun savedItemsFragmentTest() {
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.savedItemsFragment), withContentDescription("Mis articulos"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.searchItemsFragment), withContentDescription("Buscar productos"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.etSearch),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.itemsNavHostFragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("asus"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.etSearch), withText("asus"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.itemsNavHostFragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(pressImeActionButton())

        val recyclerView = onView(
            allOf(
                withId(R.id.rvSearchItems),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val floatingActionButton = onView(
            allOf(
                withId(R.id.fab),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.itemsNavHostFragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val bottomNavigationItemView3 = onView(
            allOf(
                withId(R.id.searchItemsFragment), withContentDescription("Buscar productos"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView3.perform(click())

        val recyclerView2 = onView(
            allOf(
                withId(R.id.rvSearchItems),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    1
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(1, click()))

        val floatingActionButton2 = onView(
            allOf(
                withId(R.id.fab),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.itemsNavHostFragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        floatingActionButton2.perform(click())

        val bottomNavigationItemView4 = onView(
            allOf(
                withId(R.id.savedItemsFragment), withContentDescription("Mis articulos"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView4.perform(click())

        val recyclerView3 = onView(
            allOf(
                withId(R.id.rvSavedItems),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    0
                )
            )
        )
        recyclerView3.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val bottomNavigationItemView5 = onView(
            allOf(
                withId(R.id.savedItemsFragment), withContentDescription("Mis articulos"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView5.perform(click())

        val recyclerView4 = onView(
            allOf(
                withId(R.id.rvSavedItems),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    0
                )
            )
        )
        recyclerView4.perform(actionOnItemAtPosition<ViewHolder>(1, click()))

        val bottomNavigationItemView6 = onView(
            allOf(
                withId(R.id.savedItemsFragment), withContentDescription("Mis articulos"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView6.perform(click())

        val appCompatButton = onView(
            allOf(
                withId(R.id.snackbar_action), withText("Deshacer eliminado"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.snackbar.Snackbar")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
