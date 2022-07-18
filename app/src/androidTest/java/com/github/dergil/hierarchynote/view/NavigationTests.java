package com.github.dergil.hierarchynote.view;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.dergil.hierarchynote.R;
import com.github.dergil.hierarchynote.view.activities.MainActivity;
import com.github.dergil.hierarchynote.view.activities.NoteActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NavigationTests {
    public static final String NEW_NOTE_NAME = "2Espresso";
    public static final String NEW_DIRECTORY_NAME = "DIR_Espresso";

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
        Thread.sleep(200);
    }

    @Test
    public void opensNewNoteActivity() {
        onView(withId(R.id.fab)).perform(click());
//        intended(hasComponent(NoteActivity.class.getName()));
        onView(withId(R.id.edit_text)).check(matches(isDisplayed()));

    }

    @Test
    public void opensNewDirectoryActivity() {
        onView(withId(R.id.add_dir)).perform(click());
        onView(withId(R.id.button_save)).check(matches(isDisplayed()));
    }

    @Test
    public void opensNewNoteActivityOnRecyclerViewClick() throws InterruptedException {
        insertNote();
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(NEW_NOTE_NAME)),
                        click()));
//        onView(ViewMatchers.withId(R.id.recyclerview))
//                // scrollTo will fail the test if no item matches.
//                .perform(RecyclerViewActions.scrollTo(
//                        hasDescendant(withText(NEW_NOTE_NAME))
//                )).perform(click());
//        activityScenarioRule.getScenario().onActivity(activity -> {
//            System.out.println(NoteActivity.class.getName());
//            System.out.println(activity.getClass().getSimpleName());
////            assertTrue(NoteActivity.class.getName().contains(activity.getClass().getSimpleName()));
//        });
//        intended(hasComponent(NoteActivity.class.getName()));
        onView(withId(R.id.edit_text)).check(matches(isDisplayed()));

    }

    @Test
    public void opensMainActivityOnRecyclerViewClick() {
        insertDirectory();
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(NEW_DIRECTORY_NAME)),
                        click()));
        intended(hasComponent(MainActivity.class.getName()));

//        onView(ViewMatchers.withId(R.id.recyclerview))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

//        onView(ViewMatchers.withId(R.id.recyclerview))
//                // scrollTo will fail the test if no item matches.
//                .perform(RecyclerViewActions.scrollTo(
//                        hasDescendant(withText(NEW_DIRECTORY_NAME))
//                )).perform(click());
//        activityScenarioRule.getScenario().onActivity(activity -> {
//            assertTrue(MainActivity.class.getName().contains(activity.getClass().getSimpleName()));
//        });
//        onView(withId(R.id.recyclerview))
//                .perform(scrollToPosition(0));

    }

    private void insertNote() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.edit_name))
                .perform(typeText(NEW_NOTE_NAME));
        onView(withId(R.id.edit_text))
                .perform(typeText(NEW_NOTE_NAME), closeSoftKeyboard());
        Espresso.pressBack();
    }

    private void insertDirectory() {
        onView(withId(R.id.add_dir)).perform(click());
        onView(withId(R.id.edit_name))
                .perform(typeText(NEW_DIRECTORY_NAME), closeSoftKeyboard());
        onView(withId(R.id.button_save)).perform(click());
    }
}
