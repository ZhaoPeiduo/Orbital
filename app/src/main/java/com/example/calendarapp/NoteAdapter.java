package com.example.calendarapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarapp.activity.ListReminderActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The NoteAdapter class provides adapters for Note to be used in the ListReminderActivity.
 * */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private OnItemClickListener listener;
    public List<Note> noteList = new ArrayList<>();
    private ListReminderActivity activity;
    private Timer timer;
    private List<Note> noteSource;


    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
        this.noteSource = noteList;
    }

    public Context getContext() {
        return activity;
    }

    /**
     * Access the view of the note item in the xml file and instantiate a NoteHolder.
     * @return A NoteHolder with the view obtained from note_item.xml.
     * */

    @NotNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item_cell,parent,false);
        return new NoteHolder(itemView);
    }

    /**
     * To be more efficient, update the NoteHolder with new data when the number of notes
     * displayed on the screen reaches maximum and the user swipe downward to see more notes.
     * */

    @Override
    public void onBindViewHolder(@NonNull @NotNull NoteAdapter.NoteHolder holder, int position) {
        Note note = noteList.get(position);
        holder.textViewTitle.setText(note.getTitle());
        holder.textViewDescription.setText(note.getDescription());
        holder.textViewPriority.setText(String.valueOf(note.getPriority()));
        holder.textViewDate.setText(note.getStartDate().substring(5));
        GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutNote.getBackground();
        gradientDrawable.setColor(Color.parseColor(note.getColor()));

        if(note.getImagePath() != null) {
            holder.imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
            holder.imageNote.setVisibility(View.VISIBLE);
        }
        else {
            holder.imageNote.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


    /**
     * The NoteHolder class is a customized implementation of ViewHolder, with relevant fields
     * present in the database(title, description and priority).
     * */

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;
        private TextView textViewDate;
        private LinearLayout layoutNote;
        RoundedImageView imageNote;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
            textViewDate = itemView.findViewById(R.id.text_datetime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            imageNote = itemView.findViewById(R.id.imageNote);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(noteList.get(position));
                    }
                }
            });
        }

    }

    /**
     * An interface that modules the resultant activities for a click on a note item.
     * */

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void searchNote(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(searchKeyword.trim().isEmpty()) {
                    noteList = noteSource;
                }
                else {
                    ArrayList<Note> temp = new ArrayList<>();
                    for(Note note : noteSource) {
                        if(note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                        || note.getDescription().toLowerCase().contains(searchKeyword.toLowerCase()))
                        {
                            temp.add(note);
                        }

                    }
                   noteList = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }, 500);
    }

    public void cancelTimer() {
        if(timer != null) {
            timer.cancel();
        }
    }
}
