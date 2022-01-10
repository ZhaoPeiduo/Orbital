package com.example.calendarapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.calendarapp.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

/**
 * The AddEditNoteActivity class encapsulates the add note and edit note user interfaces.
 * */

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.calendar.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.calendar.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.calendar.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.calendar.EXTRA_PRIORITY";
    public static final String EXTRA_START_DATE = "com.example.calendar.EXTRA_START_DATE";
    public static final String EXTRA_START_YEAR = "com.example.calendar.EXTRA_START_YEAR";
    public static final String EXTRA_START_MONTH = "com.example.calendar.EXTRA_START_MONTH";
    public static final String EXTRA_START_DAY = "com.example.calendar.EXTRA_START_DAY";

    public static final String EXTRA_COLOR = "com.example.calendar.EXTRA_COLOR";
    public static final String EXTRA_IMAGE_PATH = "com.example.calendar.EXTRA_IMAGE_PATH";

    public static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    public static final int ADD_NOTE_REQUEST = 2;
    public static final int REQUEST_CODE_SELECT_IMAGE = 3;

    private EditText editTextTitle;
    private EditText editTextDescription;
    private RatingBar ratingBarPriority;

    private String selectedNoteColor;
    private String selectedImagePath;


    private View viewTitleIndicator;
    private ImageView imageNote;

    /**
     * Initialise a add/edit note user interface, based on whether there is existing data
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initWidget();
        initMiscellaneous();

        setSubtitleIndicatorColor();

        Intent intent = getIntent();

        //Check whether there is existing data to decide app bar title.
        if(intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            ratingBarPriority.setRating(intent.getFloatExtra(EXTRA_PRIORITY,0));
        } else {
            setTitle("Add Note");
        }

    }

    private void initWidget() {
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        ratingBarPriority = findViewById(R.id.ratingBarPriority);
        viewTitleIndicator = findViewById(R.id.viewTitleIndicator);
        imageNote = findViewById(R.id.imageNote);

        if(getIntent().getStringExtra(EXTRA_COLOR) != null) {
            selectedNoteColor = getIntent().getStringExtra(EXTRA_COLOR);
        } else {
            selectedNoteColor = "#333333";
        }

        if(getIntent().getStringExtra(EXTRA_IMAGE_PATH) != null) {
            selectedImagePath = getIntent().getStringExtra(EXTRA_IMAGE_PATH);
        } else {
            selectedImagePath = "";
        }

    }

    /**
     * Save the user input to a new or existing note.
     * */

    private void saveNote() {
        //Obtain the data from various input portals
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        float priority = ratingBarPriority.getRating();

        //Ensure that title is non-empty
        if(title.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title", Toast.LENGTH_SHORT).show();
            return;
        }

        //Create a new intent to pass the recorded data to another activity
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE,title);
        data.putExtra(EXTRA_DESCRIPTION,description);
        data.putExtra(EXTRA_PRIORITY,priority);
        data.putExtra(EXTRA_COLOR, selectedNoteColor);
        data.putExtra(EXTRA_IMAGE_PATH, selectedImagePath);

        //The starting date information are from MainActivity(decided by the clicked date).
        //Pass these data back to MainActivity
        data.putExtra(EXTRA_START_DATE,getIntent().getStringExtra(EXTRA_START_DATE));
        data.putExtra(EXTRA_START_YEAR,getIntent().getIntExtra(EXTRA_START_YEAR, -1));
        data.putExtra(EXTRA_START_MONTH,getIntent().getIntExtra(EXTRA_START_MONTH, -1));
        data.putExtra(EXTRA_START_DAY,getIntent().getIntExtra(EXTRA_START_DAY, -1));

        //check whether the note is newly created.
        int id = getIntent().getIntExtra(EXTRA_ID, -1);//-1 for invalid id
        if(id != -1) {
            data.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK,data);
        finish();
    }

    /**
     * Instantiate the menu from the corresponding xml file.
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return true;
    }

    /**
     * Set up the link between the save button and saveNote() method.
     * */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_note) {
            saveNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMiscellaneous() {
        final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        final ImageView imageColor1 = layoutMiscellaneous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscellaneous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMiscellaneous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscellaneous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMiscellaneous.findViewById(R.id.imageColor5);
        layoutMiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#000000";
                imageColor1.setImageResource(R.drawable.ic_done);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FDBE3B";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#3A52FC";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_done);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#166A75";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_done);
                setSubtitleIndicatorColor();
            }
        });

        layoutMiscellaneous.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("this", "being clicked");
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if(ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddEditNoteActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                            ,REQUEST_CODE_STORAGE_PERMISSION);
                    Log.d("this", "request permission");
                }
                else {
                    selectImage();
                    Log.d("this", "select image being called from button");
                }
            }
        });
    }

    private void setSubtitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewTitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        Log.d("this", "select image activity started");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
                Log.d("this", "onRequestPermissionsResult being called");
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if(data != null) {
                Uri selectedImageURI = data.getData();
                if(selectedImageURI != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageURI);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageNote.setImageBitmap(bitmap);
                        imageNote.setVisibility(View.VISIBLE);
                        selectedImagePath = getPathFromUri(selectedImageURI);
                    }
                    catch(Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if(cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }
}