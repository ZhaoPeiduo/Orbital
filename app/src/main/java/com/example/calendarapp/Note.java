package com.example.calendarapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The Note class encapsulate an entity with basic attributes.
 * */

@Entity(tableName = "note_table")
public class Note {

    /**
     * Attributes of a note stored in the database.
     * The ID of a note will be automatically generated.
     * */

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private float priority;
    private String startDate;
    private String color;
    private int startYear;
    private int startMonth;
    private int startDay;
    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Note(String title, String description, float priority, String startDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.startDate = startDate;
        this.color = "#333333";
    }

    public int getStartYear() {
        return startYear;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public float getPriority() {
        return priority;
    }
}
