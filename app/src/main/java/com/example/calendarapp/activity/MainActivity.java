package com.example.calendarapp.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendarapp.CalendarAdapter;
import com.example.calendarapp.CalendarUtils;
import com.example.calendarapp.Note;
import com.example.calendarapp.NoteAdapter;
import com.example.calendarapp.NoteViewModel;
import com.example.calendarapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.calendarapp.CalendarUtils.daysInMonthArray;
import static com.example.calendarapp.CalendarUtils.monthYearFromDate;
import static com.example.calendarapp.CalendarUtils.selectedDate;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    protected static final float FLIP_DISTANCE = 50;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private GestureDetector detector;
    public static NoteViewModel noteViewModel;
    public static NoteAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        setMonthView();
        FloatingActionButton buttonViewAll = findViewById(R.id.button_view_all);
        buttonViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListReminderActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDateRep = selectedDate.toString();
                int startYear = selectedDate.getYear();
                int startMonth = selectedDate.getMonthValue();
                int startDay = selectedDate.getDayOfMonth();
                //Record the start date information
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_START_DATE,startDateRep);
                intent.putExtra(AddEditNoteActivity.EXTRA_START_YEAR,startYear);
                intent.putExtra(AddEditNoteActivity.EXTRA_START_MONTH,startMonth);
                intent.putExtra(AddEditNoteActivity.EXTRA_START_DAY,startDay);

                startActivityForResult(intent,AddEditNoteActivity.ADD_NOTE_REQUEST);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AddEditNoteActivity.ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            if(title == null) {
                return;
            }
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            float priority = data.getFloatExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);
            String date = data.getStringExtra(AddEditNoteActivity.EXTRA_START_DATE);
            Note note = new Note(title, description, priority, date);
            note.setStartYear(data.getIntExtra(AddEditNoteActivity.EXTRA_START_YEAR,-1));
            note.setStartMonth(data.getIntExtra(AddEditNoteActivity.EXTRA_START_MONTH,-1));
            note.setStartDay(data.getIntExtra(AddEditNoteActivity.EXTRA_START_DAY,-1));
            note.setColor(data.getStringExtra(AddEditNoteActivity.EXTRA_COLOR));
            note.setImagePath(data.getStringExtra(AddEditNoteActivity.EXTRA_IMAGE_PATH));

            MainActivity.noteViewModel.insert(note);

            startActivity(new Intent(this,ListReminderActivity.class));
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTv);

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
                    CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
                    setMonthView();
                    return true;
                }
                if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
                    CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
                    setMonthView();
                    return true;
                }
                return false;
            }
        });

        adapter = new NoteAdapter();

        noteViewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication()))
                .get(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNoteList(notes);
                adapter.notifyDataSetChanged();
            }
        });

        CalendarUtils.selectedDate = LocalDate.now();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setMonthView();
    }
    public void weeklyAction(View view) {
        startActivity(new Intent(this, WeekViewActivity.class));
    }

}