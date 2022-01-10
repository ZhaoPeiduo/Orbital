package com.example.calendarapp.activity;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;

import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.typeText;

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
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class ListReminderTest {

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
    public void addEditNoteTest() {

        //Attempt to select a date and click add button to add a note
        onView(withId(R.id.calendarRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(24, click()));//Note position manually chosen, likely not to be null.
        onView(withId(R.id.buttonAdd))
                .perform(click());

        //Add in test title, description and priority
        onView(withId(R.id.edit_text_title))
                .perform(typeText("TestTitle"));
        onView(withId(R.id.edit_text_description))
                .perform(typeText("TestDescription"));
        onView(withId(R.id.ratingBarPriority))
                .perform(click());

        //Add color for the note
        onView(withId(R.id.textMiscellaneous))
                .perform(click());

        onView(withId(R.id.viewColor2))
                .perform(click());
        onView(withId(R.id.viewColor2))
                .perform(click());
        onView(withId(R.id.textMiscellaneous))
                .perform(click());

        //Save the note
        onView(withId(R.id.save_note))
                .perform(click());

        //Check whether search feature works
        onView(withId(R.id.inputSearch))
                .perform(typeText("TestTitle"));
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));//Note position manually chosen, likely not to be null.

        pressBack();

        //Clear the note used for testing
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Delete all notes"))
                .perform(click());
    }
}
