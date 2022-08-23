package com.github.dergil.hierarchynote.local_tests.end_to_end;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static junit.framework.TestCase.assertFalse;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.dergil.hierarchynote.R;

import com.github.dergil.hierarchynote.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EndToEndTests {
    public static final String STRING_TO_BE_TYPED = "111End-to-End";

    @Before
    public void setUp() {
        deleteAll();
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void createNote() {
        insertNote(STRING_TO_BE_TYPED);
        onView(ViewMatchers.withId(R.id.recyclerview))
                .check(matches(atPosition(0, hasDescendant(withText(STRING_TO_BE_TYPED)))));
    }

    private void deleteAll() {
        boolean allDeleted = false;
        try {
            while (!allDeleted) {
                deleteFileAtPositionZero();
            }
        } catch (PerformException e) {
            allDeleted = true;
        }

    }

    @Test
    public void updateNote() {
        String new_name = "hro";
        String new_text = "hro_text";
        insertNote(STRING_TO_BE_TYPED);
        updateFileHelper(STRING_TO_BE_TYPED, new_name, new_text);
        onView(ViewMatchers.withId(R.id.recyclerview))
                .check(matches(atPosition(0, hasDescendant(withText(new_name)))));
    }

    @Test
    public void createDirectory() {
        Intents.init();
        onView(withId(R.id.add_dir)).perform(click());
        onView(withId(R.id.edit_name))
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.button_save)).perform(click());
        onView(ViewMatchers.withId(R.id.recyclerview))
                .check(matches(atPosition(0, hasDescendant(withText(STRING_TO_BE_TYPED)))));
    }

    @Test(expected = PerformException.class)
    public void deleteFile() {
        insertNote(STRING_TO_BE_TYPED);
        deleteFileAtPositionZero();
        deleteFileAtPositionZero();
//        assertFalse(fileAtPositionZero(STRING_TO_BE_TYPED));
    }



    private void insertNote(String name_and_text) {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.edit_name))
                .perform(typeText(name_and_text));
        onView(withId(R.id.edit_text))
                .perform(typeText(name_and_text), closeSoftKeyboard());
        Espresso.pressBack();
    }

    private void updateFileHelper(String original_name, String new_name, String text) {
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(original_name)),
                        click()));
        onView(withId(R.id.edit_name))
                .perform(replaceText(new_name));
        onView(withId(R.id.edit_text))
                .perform(replaceText(text), closeSoftKeyboard());
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

    private Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
//        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}
