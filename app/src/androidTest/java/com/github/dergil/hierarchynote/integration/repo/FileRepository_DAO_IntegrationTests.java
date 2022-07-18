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

import com.github.dergil.hierarchynote.model.dao.FileDao;
import com.github.dergil.hierarchynote.model.entity.FileEntity;
import com.github.dergil.hierarchynote.model.network.FileAPI;
import com.github.dergil.hierarchynote.model.repository.FileRepository;
import com.github.dergil.hierarchynote.model.db.FileRoomDatabase;
import com.github.dergil.hierarchynote.util.LiveDataTestUtil;
import com.github.dergil.hierarchynote.util.TestUtil;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class FileRepository_DAO_IntegrationTests {

//    @Inject
    private TestUtil testUtil = new TestUtil();

    private FileAPI networking = new FileAPI();
    private FileRoomDatabase db;
    private FileDao fileDao;
    private FileEntity note = testUtil.createNote1();
    private FileEntity note2 = testUtil.createNote2();
    private FileRepository repository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FileRoomDatabase.class).build();
        fileDao = db.fileDao();
        repository = new FileRepository(db, networking);
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertAndReadNote() throws InterruptedException {
        repository.insert(note);
        Thread.sleep(250);
        FileEntity readNote = fileDao.find(note.getId());
        assertTrue(compareNotes(note, readNote));
    }

    @Test
    public void readAll() throws InterruptedException {
        repository.insert(note);
        repository.insert(note2);
        Thread.sleep(250);
        LiveData<List<FileEntity>> liveNotes = repository.getAllFiles(note.getDirectory_name());
        List<FileEntity> readNotes = LiveDataTestUtil.getValue(liveNotes);
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
        FileEntity readNote = fileDao.find(note.getId());
        assertTrue(compareNotes(note, readNote));
    }

    @Test
    public void deleteNote() throws InterruptedException {
        repository.insert(note);
        Thread.sleep(250);
        repository.delete(note.getId());
        Thread.sleep(250);
        assertNull(fileDao.find(note.getId()));
    }
}
