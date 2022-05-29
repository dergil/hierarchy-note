package com.example.roomwordsample;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.runners.model.MultipleFailureException.assertEmpty;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {
    private NoteDao noteDao;
    private NoteRoomDatabase db;

    @Inject
    TestUtil testUtil = new TestUtil();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, NoteRoomDatabase.class).build();
        noteDao = db.wordDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertNoteAndRead() throws Exception {
        Note createdNote = new Note("name", "text", "MYDIR", false, false);
        Long id = 4242L;
        createdNote.setId(id);
        noteDao.insert(createdNote);
        Note foundNote = noteDao.find(id);
        assertTrue(compareNotes(createdNote, foundNote));
    }

    @Test
    public void insertNoteAndDelete() {
        Note insertedNote = insertNote1();
        noteDao.deleteById(insertedNote.getId());
        Note foundNote = noteDao.find(insertedNote.getId());
        assertNull(foundNote);
    }

    @Test
    public void updateNote() {
        String newText = "changed Text";
        Note insertedNote = insertNote1();
        insertedNote.setText(newText);
        noteDao.update(insertedNote);
        Note updatedNote = noteDao.find(insertedNote.getId());
        assertTrue(compareNotes(insertedNote, updatedNote));
    }

    @Test
    public void findAll() {
        Note insertedNote1 = insertNote1();
        Note insertedNote2 = insertNote2();
        List<Note> allNotes = noteDao.findAll();
        assertTrue(compareNotes(insertedNote1, allNotes.get(0)));
        assertTrue(compareNotes(insertedNote2, allNotes.get(1)));
    }

    @Test
    public void deleteAll() {
        insertNote1();
        insertNote2();
        noteDao.deleteAll();
        assertTrue(noteDao.findAll().isEmpty());
    }

    private Note insertNote1(){
        Note note = testUtil.createNote1();
        noteDao.insert(note);
        return note;
    }

    private Note insertNote2(){
        Note note = testUtil.createNote2();
        noteDao.insert(note);
        return note;
    }

    private boolean compareNotes (Note note1, Note note2) {
        boolean sameValues = true;
        if (!note1.getId().equals(note2.getId()))
            sameValues = false;
        if (!note1.getName().equals(note2.getName()))
            sameValues = false;
        if (!note1.getText().equals(note2.getText()))
            sameValues = false;
        if (!note1.isDir().equals(note2.isDir()))
            sameValues = false;
        return sameValues;
    }
}

