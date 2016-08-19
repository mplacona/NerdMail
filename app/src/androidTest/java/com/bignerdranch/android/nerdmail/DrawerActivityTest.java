package com.bignerdranch.android.nerdmail;

import android.support.design.internal.NavigationMenuItemView;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;

import com.bignerdranch.android.nerdmail.controller.DrawerActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

@RunWith(AndroidJUnit4.class)
public class DrawerActivityTest {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityRule =
            new ActivityTestRule<DrawerActivity>(DrawerActivity.class);


    @Test
    public void userSeesInboxFirst(){
        String inboxText = mActivityRule.getActivity()
                .getString(R.string.nav_drawer_inbox);
        onView(allOf(withText(inboxText),
                withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void inboxItemSelectedFirstInNavigationDrawer(){
        String inboxTest = mActivityRule.getActivity()
                .getString(R.string.nav_drawer_inbox);

        DrawerActions.openDrawer(R.id.activity_drawer_layout);

        onView(allOf(withText(inboxTest),
                withParent(isAssignableFrom(NavigationMenuItemView.class))))
                .check(matches(isChecked()));
    }

    @Test
    public void selectImportantItemShowsImportantScreen(){
        String importantText = mActivityRule.getActivity()
                .getString(R.string.nav_drawer_important);

        DrawerActions.openDrawer(R.id.activity_drawer_layout);

        // click on the "important" item in the navigation view
        onView(allOf(withText(importantText),
                withParent(isAssignableFrom(NavigationMenuItemView.class))))
                .perform(click());

        // verify that the "important" text is shows in the toolbar
        onView(allOf(withText(importantText),
                withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void selectSpamItemShowsImportantScreen(){
        String spamText = mActivityRule.getActivity()
                .getString(R.string.nav_drawer_spam);

        DrawerActions.openDrawer(R.id.activity_drawer_layout);

        // click on the "spamText" item in the navigation view
        onView(allOf(withText(spamText),
                withParent(isAssignableFrom(NavigationMenuItemView.class))))
                .perform(click());

        // verify that the "spamText" text is shows in the toolbar
        onView(allOf(withText(spamText),
                withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void selectAllItemShowsImportantScreen(){
        String allText = mActivityRule.getActivity()
                .getString(R.string.nav_drawer_all);

        DrawerActions.openDrawer(R.id.activity_drawer_layout);

        // click on the "allText" item in the navigation view
        onView(allOf(withText(allText),
                withParent(isAssignableFrom(NavigationMenuItemView.class))))
                .perform(click());

        // verify that the "allText" text is shows in the toolbar
        onView(allOf(withText(allText),
                withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(isDisplayed()));
    }
}
