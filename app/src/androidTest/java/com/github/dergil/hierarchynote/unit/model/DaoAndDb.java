package com.github.dergil.hierarchynote.unit.model;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.dergil.hierarchynote.model.db.NoteRoomDatabase;
import com.github.dergil.hierarchynote.model.entity.NoteEntity;
import com.github.dergil.hierarchynote.model.dao.NoteDao;
import com.github.dergil.hierarchynote.util.TestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import javax.inject.Inject;

@RunWith(AndroidJUnit4.class)
public class DaoAndDb {
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
    public void closeDb() {
        db.close();
    }

    @Test
    public void readFile() throws Exception {
        NoteEntity createdNote = new NoteEntity("name", "text", "MYDIR", false, false);
        Long id = 4242L;
        createdNote.setId(id);
        noteDao.insert(createdNote);
        NoteEntity foundNote = noteDao.find(id);
        assertTrue(TestUtil.compareNotes(createdNote, foundNote));
    }

    @Test
    public void deleteFile() {
        NoteEntity insertedNote = insertNote1();
        noteDao.deleteById(insertedNote.getId());
        NoteEntity foundNote = noteDao.find(insertedNote.getId());
        assertNull(foundNote);
    }

    @Test
    public void updateFile() {
        String newText = "changed Text";
        NoteEntity insertedNote = insertNote1();
        insertedNote.setText(newText);
        noteDao.update(insertedNote);
        NoteEntity updatedNote = noteDao.find(insertedNote.getId());
        assertTrue(TestUtil.compareNotes(insertedNote, updatedNote));
    }

    @Test
    public void findAll() {
        NoteEntity insertedNote1 = insertNote1();
        NoteEntity insertedNote2 = insertNote2();
        List<NoteEntity> allNotes = noteDao.findAll();
        assertTrue(TestUtil.compareNotes(insertedNote1, allNotes.get(0)));
        assertTrue(TestUtil.compareNotes(insertedNote2, allNotes.get(1)));
    }

    @Test
    public void deleteAll() {
        insertNote1();
        insertNote2();
        noteDao.deleteAll();
        assertTrue(noteDao.findAll().isEmpty());
    }

    private NoteEntity insertNote1(){
        NoteEntity note = testUtil.createNote1();
        noteDao.insert(note);
        return note;
    }

    private NoteEntity insertNote2(){
        NoteEntity note = testUtil.createNote2();
        noteDao.insert(note);
        return note;
    }
}

