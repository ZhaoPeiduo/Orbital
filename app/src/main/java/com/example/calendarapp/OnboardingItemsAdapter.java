package com.example.calendarapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OnboardingItemsAdapter extends RecyclerView.Adapter<OnboardingItemsAdapter.OnboardingItemViewHolder> {
    List<OnboardingItem> onboardingItems;
    public OnboardingItemsAdapter(List<OnboardingItem> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingItemViewHolder holder, int position) {
        holder.bind(onboardingItems.get(position));
    }

    @NonNull
    @Override
    public OnboardingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.onboarding_item_container, parent,false));
    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    static class OnboardingItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imageOnboarding;
        TextView textTitle;
        TextView textDescription;
        public OnboardingItemViewHolder(View view) {
            super(view);
            imageOnboarding = view.findViewById(R.id.imageOnboarding);
            textTitle = view.findViewById(R.id.textTitle);
            textDescription = view.findViewById(R.id.textDescription);
        }
        public void bind(OnboardingItem onboardingItem) {
            imageOnboarding.setImageResource(onboardingItem.onboardingImage);
            textTitle.setText(onboardingItem.title);
            textDescription.setText(onboardingItem.description);
        }


    }
}
