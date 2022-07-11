package com.github.dergil.hierarchynote;

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

import static com.github.dergil.hierarchynote.ChangeTextBehaviorTest.atPosition;

import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.github.dergil.hierarchynote.model.entity.NoteEntity;
import com.github.dergil.hierarchynote.view.activities.MainActivity;
import com.github.dergil.hierarchynote.view.activities.NewDirectory;
import com.github.dergil.hierarchynote.view.activities.NoteActivity;
import com.github.dergil.hierarchynote.viewmodel.NoteViewModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTests {
    public static final String STRING_TO_BE_TYPED = "2Espresso";

//    @Inject
//    NoteRepository noteRepository;


    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void displaysNewNoteButton() {
        Intents.init();
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
    }

    @Test
    public void OpensNewNoteActivity() {
        Intents.init();
        onView(withId(R.id.fab)).perform(click());

        intended(hasComponent(NoteActivity.class.getName()));
    }

    @Test
    public void OpensNewDirectoryActivity() {
        Intents.init();
        onView(withId(R.id.add_dir)).perform(click());

        intended(hasComponent(NewDirectory.class.getName()));
    }

    @Test
//    only works when there is a note at position 0
    public void OpensNewNoteActivityOnRecyclerViewClick() {
        Intents.init();

        onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(NoteActivity.class.getName()));
    }

    @Test
//    only works when there is a dir at position 2
    public void OpensMainActivityOnRecyclerViewClick() {
        Intents.init();

        onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        intended(hasComponent(MainActivity.class.getName()));
    }



//    @Test(expected = PerformException.class)
    @Test
    public void itemWithText_doesNotExist() {
        NoteViewModel mNoteViewModel = new ViewModelProvider(ApplicationProvider.getApplicationContext()).get(NoteViewModel.class);
        mNoteViewModel.insert(new NoteEntity("name", "text", "MYDIR", false, false));

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
