package com.github.dergil.hierarchynote.end_to_end;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.github.dergil.hierarchynote.ChangeTextBehaviorTest.atPosition;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.dergil.hierarchynote.R;

import com.github.dergil.hierarchynote.view.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EndToEndTests {
    public static final String STRING_TO_BE_TYPED = "12Espresso";

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

//    @Before
//    public void setUp() throws Exception {
//        Intents.init();
//    }
//
//    @After
//    public void tearDown() {
//        if (fileAtPositionZero(STRING_TO_BE_TYPED))
//            deleteFileAtPositionZero();
//    }


    @Test
    public void createNote() {
        insertNote();
        onView(ViewMatchers.withId(R.id.recyclerview))
                .check(matches(atPosition(0, hasDescendant(withText(STRING_TO_BE_TYPED)))));
//        onView(ViewMatchers.withId(R.id.recyclerview))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));
//        onView(ViewMatchers.withId(R.id.recyclerview))
//                .check(matches(not(atPosition(0, hasDescendant(withText(STRING_TO_BE_TYPED))))));
//        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void createDirectory() {
        Intents.init();
        onView(withId(R.id.add_dir)).perform(click());
        onView(withId(R.id.edit_name))
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.button_save)).perform(click());
//        Thread.sleep(2000);
        onView(ViewMatchers.withId(R.id.recyclerview))
                .check(matches(atPosition(0, hasDescendant(withText(STRING_TO_BE_TYPED)))));
//        assertTrue(fileAtPositionZero(STRING_TO_BE_TYPED));
    }

    @Test
    public void deleteFile() {
        insertNote();
        deleteFileAtPositionZero();
        assertFalse(fileAtPositionZero(STRING_TO_BE_TYPED));
    }



    private void insertNote() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.edit_name))
                .perform(typeText(STRING_TO_BE_TYPED));
        onView(withId(R.id.edit_text))
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
        Espresso.pressBack();
    }

    private boolean fileAtPositionZero(String name) {
        return ViewMatchers.withId(R.id.recyclerview)
                .matches(atPosition(0, hasDescendant(withText(name))));
    }

    private void deleteFileAtPositionZero() {
        onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));
    }
}
