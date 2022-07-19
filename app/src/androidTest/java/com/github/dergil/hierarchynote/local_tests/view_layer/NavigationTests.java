package com.github.dergil.hierarchynote.local_tests.view_layer;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
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
public class NavigationTests {
    public static final String NEW_NOTE_NAME = "2Espresso";
    public static final String NEW_DIRECTORY_NAME = "DIR_Espresso";

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @BeforeClass
    public static void beforeClass() throws Exception {
        Intents.release();
        Thread.sleep(250);
        Intents.init();

    }

//    @Before
//    public void setUp() {
////        Intents.release();
//        Intents.init();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        Intents.release();
//        Thread.sleep(200);
//    }

    @Test
    public void opensNewNoteActivity() throws InterruptedException {
        onView(withId(R.id.fab)).perform(click());
//        intended(hasComponent(NoteActivity.class.getName()));
        Thread.sleep(250);
        onView(withId(R.id.edit_text)).check(matches(isDisplayed()));

    }

    @Test
    public void opensNewDirectoryActivity() throws InterruptedException {
        onView(withId(R.id.add_dir)).perform(click());
        Thread.sleep(250);
        onView(withId(R.id.button_save)).check(matches(isDisplayed()));
    }

    @Test
    public void opensNewNoteActivityOnRecyclerViewClick() throws InterruptedException {
        insertNote();
        Thread.sleep(250);
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(NEW_NOTE_NAME)),
                        click()));
        onView(withId(R.id.edit_text)).check(matches(isDisplayed()));

    }

    @Test
    public void opensMainActivityOnRecyclerViewClick() throws InterruptedException {
        insertDirectory();
        Thread.sleep(250);
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(NEW_DIRECTORY_NAME)),
                        click()));
        intended(hasComponent(MainActivity.class.getName()));
    }

    private void insertNote() throws InterruptedException {
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(250);
        onView(withId(R.id.edit_name))
                .perform(typeText(NEW_NOTE_NAME));
        onView(withId(R.id.edit_text))
                .perform(typeText(NEW_NOTE_NAME), closeSoftKeyboard());
        Espresso.pressBack();
    }

    private void insertDirectory() throws InterruptedException {
        onView(withId(R.id.add_dir)).perform(click());
        Thread.sleep(250);
        onView(withId(R.id.edit_name))
                .perform(typeText(NEW_DIRECTORY_NAME), closeSoftKeyboard());
        onView(withId(R.id.button_save)).perform(click());
    }
}
