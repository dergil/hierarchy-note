//package com.example.roomwordsample;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//
//import android.content.Context;
//
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.filters.LargeTest;
//import androidx.test.platform.app.InstrumentationRegistry;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static org.junit.Assert.*;
//import static android.support.test.espresso.intent.Intents.intended;
//import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
//
//
//
//
///**
// * Instrumented test, which will execute on an Android device.
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class ExampleInstrumentedTest {
//
//    @Rule
//    public ActivityScenarioRule<MainActivity> activityScenarioRule
//            = new ActivityScenarioRule<>(MainActivity.class);
//
//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
////        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
////        assertEquals("com.example.roomwordsample", appContext.getPackageName());
//
//
//        onView(withId(R.id.fab)).perform(click());
////        intended(hasComponent(YourExpectedActivity.class.getName()));
//    }
//}