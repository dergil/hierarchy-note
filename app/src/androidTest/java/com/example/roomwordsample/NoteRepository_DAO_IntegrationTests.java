package com.example.roomwordsample;

import static com.example.roomwordsample.TestUtil.compareNotes;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

@RunWith(AndroidJUnit4.class)
public class NoteRepository_DAO_IntegrationTests {

    @Inject
    private TestUtil testUtil = new TestUtil();

    private Networking networking = new Networking();
    private NoteRoomDatabase db;
    private NoteDao noteDao;
    private Note note = testUtil.createNote1();
    private NoteRepository repository;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, NoteRoomDatabase.class).build();
        noteDao = db.wordDao();
        repository = new NoteRepository(db, networking);
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertAndReadNote() throws InterruptedException {
        repository.insert(note);
        Thread.sleep(50);
        Note readNote = noteDao.find(note.getId());
        assertTrue(compareNotes(note, readNote));
    }

    @Test
    public void updateNote() throws InterruptedException {
        repository.insert(note);
        Thread.sleep(50);
        note.setText("new Text");
        repository.update(note);
        Thread.sleep(50);
        Note readNote = noteDao.find(note.getId());
        assertTrue(compareNotes(note, readNote));
    }

    @Test
    public void deleteNote() throws InterruptedException {
        repository.insert(note);
        Thread.sleep(50);
        repository.delete(note.getId());
        Thread.sleep(50);
        assertNull(noteDao.find(note.getId()));
    }
}
