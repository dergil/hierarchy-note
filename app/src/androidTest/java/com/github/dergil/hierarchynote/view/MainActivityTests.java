package com.github.dergil.hierarchynote.view;

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

import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.dergil.hierarchynote.R;
import com.github.dergil.hierarchynote.model.entity.FileEntity;
import com.github.dergil.hierarchynote.view.activities.MainActivity;
import com.github.dergil.hierarchynote.viewmodel.FileViewModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTests {
    public static final String NEW_NOTE_NAME = "2Espresso";
    public static final String NEW_DIRECTORY_NAME = "DIR_Espresso";

    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void displaysUiElements() {
        Intents.init();
        onView(ViewMatchers.withId(R.id.fab)).check(matches(isDisplayed()));
        onView(withId(R.id.add_dir)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview)).check(matches(isDisplayed()));
    }



//    @Test(expected = PerformException.class)
    @Test
    public void itemWithText_doesNotExist() {
        FileViewModel mFileViewModel = new ViewModelProvider(ApplicationProvider.getApplicationContext()).get(FileViewModel.class);
        mFileViewModel.insert(new FileEntity("name", "text", "MYDIR", false, false));

//        final String STRING_TO_BE_TYPED = "Delete";
//
//        onView(withId(R.id.fab)).perform(click());
//
//        onView(withId(R.id.edit_name))
//                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.edit_text))
//                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.button_save)).perform(click());
//
//        // Attempt to scroll to an item that contains the special text.
//        onView(ViewMatchers.withId(R.id.recyclerview))
//                // scrollTo will fail the test if no item matches.
//                .perform(RecyclerViewActions.scrollTo(
//                        hasDescendant(withText(STRING_TO_BE_TYPED))
//                ));
    }


}
