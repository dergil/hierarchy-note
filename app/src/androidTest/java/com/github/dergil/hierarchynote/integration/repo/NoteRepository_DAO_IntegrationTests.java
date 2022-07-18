package com.github.dergil.hierarchynote.integration.repo;

import static com.github.dergil.hierarchynote.util.TestUtil.compareNotes;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.github.dergil.hierarchynote.model.network.NoteAPI;
import com.github.dergil.hierarchynote.model.entity.NoteEntity;
import com.github.dergil.hierarchynote.model.dao.NoteDao;
import com.github.dergil.hierarchynote.model.repository.NoteRepository;
import com.github.dergil.hierarchynote.model.db.NoteRoomDatabase;
import com.github.dergil.hierarchynote.util.LiveDataTestUtil;
import com.github.dergil.hierarchynote.util.TestUtil;

import java.util.List;

import javax.inject.Inject;

@RunWith(AndroidJUnit4.class)
public class NoteRepository_DAO_IntegrationTests {

//    @Inject
    private TestUtil testUtil = new TestUtil();

    private NoteAPI networking = new NoteAPI();
    private NoteRoomDatabase db;
    private NoteDao noteDao;
    private NoteEntity note = testUtil.createNote1();
    private NoteEntity note2 = testUtil.createNote2();
    private NoteRepository repository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

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
        Thread.sleep(250);
        NoteEntity readNote = noteDao.find(note.getId());
        assertTrue(compareNotes(note, readNote));
    }

    @Test
    public void readAll() throws InterruptedException {
        repository.insert(note);
        repository.insert(note2);
        Thread.sleep(250);
        LiveData<List<NoteEntity>> liveNotes = repository.getAllWords(note.getDirectory_name());
        List<NoteEntity> readNotes = LiveDataTestUtil.getValue(liveNotes);
        assertTrue(compareNotes(note, readNotes.get(0)));
        assertTrue(compareNotes(note2, readNotes.get(1)));

    }

    @Test
    public void updateNote() throws InterruptedException {
        repository.insert(note);
        Thread.sleep(250);
        note.setText("new Text");
        repository.update(note);
        Thread.sleep(250);
        NoteEntity readNote = noteDao.find(note.getId());
        assertTrue(compareNotes(note, readNote));
    }

    @Test
    public void deleteNote() throws InterruptedException {
        repository.insert(note);
        Thread.sleep(250);
        repository.delete(note.getId());
        Thread.sleep(250);
        assertNull(noteDao.find(note.getId()));
    }
}
