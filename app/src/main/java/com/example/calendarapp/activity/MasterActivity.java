package com.example.calendarapp.activity;
import com.example.calendarapp.OnboardingItem;
import com.example.calendarapp.OnboardingItemsAdapter;
import com.example.calendarapp.R;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Arrays;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MasterActivity extends AppCompatActivity {
    private OnboardingItemsAdapter onboardingItemsAdapter;
    private LinearLayout indicatorsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        setOnboardingItems();
        setupIndicators();
        setCurrentIndicator(0);
    }

    private void setOnboardingItems() {
        onboardingItemsAdapter = new OnboardingItemsAdapter(Arrays.asList(
                new OnboardingItem(R.drawable.calendar,
                        "Calendar",
                        "Add events to a specific date by clicking the add button"),
                new OnboardingItem(R.drawable.list,
                        "View Events",
                        "View all events in the list, search note at the top"),
                new OnboardingItem(R.drawable.swipe,
                        "Actions to manipulate events",
                        "Swipe right: delete; Swipe left: add to notification centre; Click: edit")
        ));
        ViewPager2 onboardingViewPager = findViewById(R.id.onboardingViewPager);
        onboardingViewPager.setAdapter(onboardingItemsAdapter);
        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
        RecyclerView recyclerView = (RecyclerView) onboardingViewPager.getChildAt(0);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        findViewById(R.id.imageNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onboardingViewPager.getCurrentItem() + 1 < onboardingItemsAdapter.getItemCount()) {
                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);
                }
                else {
                    navigateToTargetActivity();
                }
            }
        });

        findViewById(R.id.textSkip).setOnClickListener(v -> {
            navigateToTargetActivity();
        });

        findViewById(R.id.buttonGetStarted).setOnClickListener(v -> {
            navigateToTargetActivity();
        });
    }

    private void setupIndicators() {
        indicatorsContainer = findViewById(R.id.indicatorContainer);
        ImageView[] indicators = new ImageView[onboardingItemsAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.setMargins(8,0,8,0);
        for(int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.indicator_inactive_background));
            indicators[i].setLayoutParams(layoutParams);
            indicatorsContainer.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int position) {
        int childCount = indicatorsContainer.getChildCount();
        for(int i = 0; i < childCount;i++) {
            ImageView imageView = (ImageView) indicatorsContainer.getChildAt(i);
            if(i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.indicator_active_background
                ));
            }
            else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.indicator_inactive_background
                ));

            }
        }
    }

    private void navigateToTargetActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}