package com.github.dergil.hierarchynote.local_tests.view_layer;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.dergil.hierarchynote.R;
import com.github.dergil.hierarchynote.view.activities.NewDirectoryActivity;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NewDirectoryActivityTests {

    @Rule
    public ActivityScenarioRule<NewDirectoryActivity> activityScenarioRule
            = new ActivityScenarioRule<>(NewDirectoryActivity.class);

    @BeforeClass
    public static void beforeClass() throws Exception {
        Intents.release();
        Thread.sleep(50);
        Intents.init();
    }

    @Test
    public void displaysUiElements() {
//        Intents.init();
        onView(ViewMatchers.withId(R.id.edit_name)).check(matches(isDisplayed()));
        onView(withId(R.id.button_save)).check(matches(isDisplayed()));
    }
}
