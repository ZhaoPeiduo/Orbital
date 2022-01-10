package com.example.calendarapp.activity;

import androidx.test.espresso.Espresso;

import androidx.test.espresso.contrib.RecyclerViewActions;
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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);


    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void mainActivityTest() {
        //Check whether the calendar and buttons are clickable and fully displayed.
        onView(withId(R.id.buttonAdd))
                .check(matches(isClickable()));
        onView(withId(R.id.button_view_all))
                .check(matches(isClickable()));
        onView(withId(R.id.buttonWeekly))
                .check(matches(isClickable()));
        onView(withId(R.id.buttonAdd))
                .check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.button_view_all))
                .check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.calendarRecyclerView))
                .check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.buttonWeekly))
                .check(matches(isCompletelyDisplayed()));

        //Check if a different date on the calendar can be selected.
        onView(withId(R.id.calendarRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(24, click()));//Note position manually chosen, likely not to be null.
        onView(withId(R.id.buttonAdd))
                .perform(click());

        //Check if the intent starting AddEditNoteActivity and ListReminderActivity works
        Intents.intended(hasComponent(ComponentNameMatchers.hasClassName(AddEditNoteActivity.class.getName())));
        //Return to MainActivity after checking intent
        Espresso.pressBack();
        onView(withId(R.id.button_view_all))
                .perform(click());
        Intents.intended(hasComponent(ComponentNameMatchers.hasClassName(ListReminderActivity.class.getName())));
        Espresso.pressBack();
        onView(withId(R.id.buttonWeekly))
                .perform(click());
        Intents.intended(hasComponent(ComponentNameMatchers.hasClassName(WeekViewActivity.class.getName())));
        Espresso.pressBack();
    }
}

