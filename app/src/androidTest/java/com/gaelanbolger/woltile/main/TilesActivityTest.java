package com.gaelanbolger.woltile.main;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.gaelanbolger.woltile.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
@LargeTest
public class TilesActivityTest {

    @Rule
    public ActivityTestRule<TilesActivity> mTilesActivityTestRule =
            new ActivityTestRule<>(TilesActivity.class);

    @Test
    public void clickFirstRecyclerViewItem_opensEditTileUi() throws Exception {
        onView(withId(R.id.rv_tile)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.et_host_name)).check(matches(isDisplayed()));
    }

    @Test
    public void checkTileCreationAndDeletion() throws Exception {
        // Tile is not already enabled
        onView(withId(R.id.rv_tile)).check(matches(atPosition(0, hasDescendant(not(isEnabled())))));

        // Click the first tile
        onView(withId(R.id.rv_tile)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check that the Host Name EditText is displayed
        onView(withId(R.id.et_host_name)).check(matches(isDisplayed()));

        // Fill in required Host fields and click save MenuItem
        onView(withId(R.id.et_host_name)).perform(typeText("Test Host"));
        onView(withId(R.id.et_ip_address)).perform(typeText("10.0.0.1"));
        onView(withId(R.id.et_mac_address)).perform(typeText("000000000000"));
        onView(withId(R.id.item_save)).perform(click());

        // Check that the tile is now enabled
        onView(withId(R.id.rv_tile)).check(matches(atPosition(0, isEnabled())));

        // Perform a long click on the tile, select the "Delete" option, and confirm deletion
        onView(withId(R.id.rv_tile)).perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
        onView(withText("Delete")).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        // check that the tile is disabled again
        onView(withId(R.id.rv_tile)).check(matches(atPosition(0, hasDescendant(not(isEnabled())))));
    }
}
