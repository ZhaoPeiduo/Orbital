package com.example.calendarapp.activity;

import androidx.test.espresso.intent.Intents;

import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import androidx.test.espresso.intent.matcher.ComponentNameMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.calendarapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MasterTest {

    @Rule
    public ActivityScenarioRule<MasterActivity> activityRule
            = new ActivityScenarioRule<>(MasterActivity.class);


    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void masterActivitySkipTest() {

        //See if all three pagers can be displayed
        onView(withId(R.id.imageNext))
                .perform(click());
        onView(withId(R.id.imageNext))
                .perform(click());

        onView(withId(R.id.textSkip))
                .perform(click());

        //check if skip navigate to MainActivity.
        Intents.intended(hasComponent(ComponentNameMatchers.hasClassName(MainActivity.class.getName())));

    }

    @Test
    public void masterActivityGetStartedTest() {
        onView(withId(R.id.buttonGetStarted))
                .perform(click());

        //check if skip navigate to MainActivity.
        Intents.intended(hasComponent(ComponentNameMatchers.hasClassName(MainActivity.class.getName())));

    }
}
