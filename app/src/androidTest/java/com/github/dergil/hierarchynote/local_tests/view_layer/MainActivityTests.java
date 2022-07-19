package com.github.dergil.hierarchynote.local_tests.view_layer;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.dergil.hierarchynote.R;
import com.github.dergil.hierarchynote.view.activities.MainActivity;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTests {

    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @BeforeClass
    public static void beforeClass() throws Exception {
        Intents.release();
        Thread.sleep(50);
        Intents.init();

    }

    @Test
    public void displaysUiElements() {
        onView(ViewMatchers.withId(R.id.fab)).check(matches(isDisplayed()));
        onView(withId(R.id.add_dir)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview)).check(matches(isDisplayed()));
    }
}
