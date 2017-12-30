package com.gaelanbolger.woltile.main;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.gaelanbolger.woltile.R;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.gaelanbolger.woltile.util.Utils.atPosition;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class TilesActivityTest {

    @Rule
    public ActivityTestRule<TilesActivity> mTilesActivityTestRule =
            new ActivityTestRule<>(TilesActivity.class);

    @Test
    public void test1_clickFirstRecyclerViewItem_opensEditTileUi() throws Exception {
        onView(withId(R.id.rv_tile)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.et_host_name)).check(matches(isDisplayed()));
    }

    @Test
    public void test2_enableTileAfterHostSelection() throws Exception {
        onView(withId(R.id.rv_tile)).check(matches(atPosition(0, hasDescendant(not(isEnabled())))));

        onView(withId(R.id.rv_tile)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.et_host_name)).check(matches(isDisplayed()));

        onView(withId(R.id.et_host_name)).perform(typeText("Test Host"));
        onView(withId(R.id.et_ip_address)).perform(typeText("10.0.0.1"));
        onView(withId(R.id.et_mac_address)).perform(typeText("000000000000"));
        onView(withId(R.id.item_save)).perform(click());

        onView(withId(R.id.rv_tile)).check(matches(atPosition(0, isEnabled())));
    }

    @Test
    public void test3_disableTileAfterHostDeletion() throws Exception {
        onView(withId(R.id.rv_tile)).check(matches(atPosition(0, isEnabled())));

        onView(withId(R.id.rv_tile)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
        onView(withText("Delete")).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.rv_tile)).check(matches(atPosition(0, hasDescendant(not(isEnabled())))));
    }
}
