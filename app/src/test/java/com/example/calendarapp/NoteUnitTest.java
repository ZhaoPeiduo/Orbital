package com.example.calendarapp;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class NoteUnitTest {
    @Test
    public void noteUnitTest() {
        Note testNote1 = new Note("Title", "Description", 1f,"2021-07-01");
        Note testNote2 = new Note("Test",null,1.5f, "1970-01-01");
        assertEquals("Title",testNote1.getTitle());
        assertEquals("Description",testNote1.getDescription());
        assertEquals(1f,testNote1.getPriority(),0f);
        assertEquals("2021-07-01",testNote1.getStartDate());
        assertEquals("Test",testNote2.getTitle());
        assertNull(testNote2.getDescription());
        assertEquals(1.5f,testNote2.getPriority());
        assertEquals("1970-01-01",testNote2.getStartDate());
    }
}
