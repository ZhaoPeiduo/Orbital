package com.example.calendarapp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarapp.activity.MainActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>{

    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;


    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener) {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(days.size() > 15) //month view
            layoutParams.height = (int) (parent.getHeight() * 0.1666666);
        else // week view
            layoutParams.height = (int) parent.getHeight();


        return new CalendarViewHolder(view, onItemListener, days);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);
        if(date == null) {
            holder.dayOfMonth.setText("");
        }
        else {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

            if(date.equals(CalendarUtils.selectedDate)) {
                ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
                drawable.setTint((Color.parseColor("#643AE7")));
                holder.parentView.setBackground(drawable);
            }
            else if(date.equals(CalendarUtils.today)) {
                ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
                drawable.setTint((Color.LTGRAY));
                holder.parentView.setBackground(drawable);
            }

            List<Note> notes = MainActivity.noteViewModel.getAllNotes().getValue();
            if(notes != null) {
                for(Note note : notes) {
                    LocalDate dateWithTask = LocalDate.of(note.getStartYear(), note.getStartMonth(), note.getStartDay());
                    if(date.equals(dateWithTask)) {
                        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
                        drawable.setTint((Color.parseColor(note.getColor())));
                        holder.parentView.setBackground(drawable);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, LocalDate date);
    }
}
