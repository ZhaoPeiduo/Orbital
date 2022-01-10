package com.example.calendarapp.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.calendarapp.CalendarAdapter;
import com.example.calendarapp.CalendarUtils;
import com.example.calendarapp.Note;
import com.example.calendarapp.NoteAdapter;
import com.example.calendarapp.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.calendarapp.CalendarUtils.daysInWeekArray;
import static com.example.calendarapp.CalendarUtils.monthYearFromDate;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private GestureDetector detector;
    private List<Note> notesToShow;
    protected static final float FLIP_DISTANCE = 50;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        initWidgets();
        setWeekView();
        detector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
                    CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
                    setWeekView();
                    return true;
                }
                if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
                    CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
                    setWeekView();
                    return true;
                }
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTv);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
        notesToShow = new ArrayList<>();
        List<Note> savedNotes = MainActivity.noteViewModel.getAllNotes().getValue();
        if(savedNotes!= null) {
            for (Note note : savedNotes) {
                if (date.getYear() == note.getStartYear() &&
                        date.getMonthValue() == note.getStartMonth() &&
                        date.getDayOfMonth() == note.getStartDay()) {
                    notesToShow.add(note);
                }
            }
            showNotes();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    private void showNotes() {
        RecyclerView recyclerView = findViewById(R.id.weeklyNotesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setNoteList(notesToShow);
    }
}