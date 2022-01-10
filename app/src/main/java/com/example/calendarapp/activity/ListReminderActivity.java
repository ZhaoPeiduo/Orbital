package com.example.calendarapp.activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.calendarapp.Note;
import com.example.calendarapp.NoteAdapter;
import com.example.calendarapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.calendarapp.activity.MainActivity.noteViewModel;

/**
 * The ListReminderActivity class encapsulates the user interface that displays the saved notes.
 * */

public class ListReminderActivity extends AppCompatActivity {
    public static final int EDIT_NOTE_REQUEST = 4;

    /**
     * Initialise the ViewModel, RecyclerView and add note button.
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_reminder);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(MainActivity.adapter);

        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.adapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(MainActivity.adapter.getItemCount() != 0)
                    MainActivity.adapter.searchNote(s.toString());

            }
        });

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                MainActivity.adapter.setNoteList(notes);
                MainActivity.adapter.notifyDataSetChanged();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView, RecyclerView.@NotNull ViewHolder viewHolder, RecyclerView.@NotNull ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.@NotNull ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                if(direction == ItemTouchHelper.RIGHT) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListReminderActivity.this);
                    builder.setTitle("Delete Task")
                            .setMessage("Are you sure ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    noteViewModel.delete(MainActivity.adapter.noteList.get(viewHolder.getAdapterPosition()));
                                    Toast.makeText(ListReminderActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.adapter.notifyItemChanged(position);
                                }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if(direction == ItemTouchHelper.LEFT) {
                    String ContentTitle = MainActivity.adapter.noteList.get(viewHolder.getAdapterPosition()).getTitle() + ": " +
                            MainActivity.adapter.noteList.get(viewHolder.getAdapterPosition()).getDescription();
                    String ContentText= MainActivity.adapter.noteList.get(viewHolder.getAdapterPosition()).getStartDate();
                    int ID = 100;
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification notification;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("1","channel",NotificationManager.IMPORTANCE_LOW);
                        manager.createNotificationChannel(channel);
                        Bitmap bitmap = BitmapFactory.decodeFile(MainActivity.adapter.noteList.get(viewHolder.getAdapterPosition()).getImagePath());
                        if (bitmap != null) {
                            notification = new Notification.Builder(ListReminderActivity.this, "1")
                                    .setCategory(Notification.CATEGORY_MESSAGE)
                                    .setSmallIcon(R.drawable.ic_notification)
                                    .setContentTitle(ContentTitle)
                                    .setContentText(ContentText)
                                    .setLargeIcon(bitmap)
                                    .setAutoCancel(true)
                                    .build();
                        } else {
                            notification = new Notification.Builder(ListReminderActivity.this, "1")
                                    .setCategory(Notification.CATEGORY_MESSAGE)
                                    .setSmallIcon(R.drawable.ic_notification)
                                    .setContentTitle(ContentTitle)
                                    .setContentText(ContentText)
                                    .setAutoCancel(true)
                                    .build();
                        }

                    } else {
                        Bitmap bitmap = BitmapFactory.decodeFile(MainActivity.adapter.noteList.get(viewHolder.getAdapterPosition()).getImagePath());
                        if (bitmap != null) {
                            notification = new Notification.Builder(ListReminderActivity.this)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle(ContentTitle)
                                    .setContentText(ContentText)
                                    .setLargeIcon(bitmap)
                                    .build();
                        } else {
                            notification = new Notification.Builder(ListReminderActivity.this)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle(ContentTitle)
                                    .setContentText(ContentText)
                                    .build();
                        }
                    }
                    manager.notify(ID, notification);
                    MainActivity.adapter.notifyItemChanged(position);
                }
            }
        }).attachToRecyclerView(recyclerView);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                MainActivity.adapter.setNoteList(notes);
            }
        });

        MainActivity.adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(ListReminderActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
                intent.putExtra(AddEditNoteActivity.EXTRA_START_DATE, note.getStartDate());
                intent.putExtra(AddEditNoteActivity.EXTRA_START_YEAR,note.getStartYear());
                intent.putExtra(AddEditNoteActivity.EXTRA_START_MONTH,note.getStartMonth());
                intent.putExtra(AddEditNoteActivity.EXTRA_START_DAY,note.getStartDay());
                intent.putExtra(AddEditNoteActivity.EXTRA_COLOR, note.getColor());
                intent.putExtra(AddEditNoteActivity.EXTRA_IMAGE_PATH, note.getImagePath());
                startActivityForResult(intent,EDIT_NOTE_REQUEST);
            }
        });

    }


    /**
     * Start corresponding activities for the given requestCode and resultCode.
     * */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);
            if(id == -1) {
                Toast.makeText(this, "Invalid ID", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            float priority = data.getFloatExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);
            String date = data.getStringExtra(AddEditNoteActivity.EXTRA_START_DATE);
            Note note = new Note(title,description,priority,date);
            note.setId(id);
            note.setStartYear(data.getIntExtra(AddEditNoteActivity.EXTRA_START_YEAR, -1));
            note.setStartMonth(data.getIntExtra(AddEditNoteActivity.EXTRA_START_MONTH, -1));
            note.setStartDay(data.getIntExtra(AddEditNoteActivity.EXTRA_START_DAY, -1));
            note.setColor(data.getStringExtra(AddEditNoteActivity.EXTRA_COLOR));
            note.setImagePath(data.getStringExtra(AddEditNoteActivity.EXTRA_IMAGE_PATH));
            noteViewModel.update(note);
            Toast.makeText(this, "Note edited", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Instantiate the menu from the corresponding xml file.
     * */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.list_reminder_menu,menu);
        return true;
    }

    /**
     * Set up the link between the deleteAllNotes option in the menu with deleteAllNotes() method.
     * */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_notes) {
            noteViewModel.deleteAllNotes();
            Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
