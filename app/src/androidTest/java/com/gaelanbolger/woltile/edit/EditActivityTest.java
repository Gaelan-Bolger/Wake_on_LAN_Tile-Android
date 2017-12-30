package com.gaelanbolger.woltile;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.gaelanbolger.woltile.edit.EditActivity;
import com.gaelanbolger.woltile.qs.TileComponent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditActivityTest {

    private TileComponent mTileComponent = TileComponent.values()[0];

    @Rule
    public ActivityTestRule<EditActivity> mEditActivityTestRule = new ActivityTestRule<EditActivity>(EditActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra(EditActivity.EXTRA_TILE_COMPONENT, mTileComponent.name());
            return intent;
        }
    };

    @Test
    public void checkTitle() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String componentTitle = context.getString(mTileComponent.getTitleResId());
        onView(allOf(instanceOf(TextView.class), withParent(instanceOf(Toolbar.class))))
                .check(matches(withText(componentTitle)));
    }

    @Test
    public void shouldShowHostNameError() throws Exception {
        onView(withId(R.id.item_save)).perform(click());
        onView(withId(R.id.et_host_name)).check(matches(hasErrorText("Host Name is required")));
    }

    @Test
    public void checkIpAddressFilter() throws Exception {
        onView(withId(R.id.et_ip_address))
                .perform(typeText("Aa192..155++.34.255-9999"))
                .check(matches(withText("192.155.34.255")));
    }

    @Test
    public void checkMacAddressFilter() throws Exception {
        onView(withId(R.id.et_mac_address))
                .perform(click())
                .perform(clearText())
                .perform(typeText("Aa192.5BdgfmZE++.3-9999"))
                .check(matches(withText("AA:19:25:BD:FE:39")));
    }
}
