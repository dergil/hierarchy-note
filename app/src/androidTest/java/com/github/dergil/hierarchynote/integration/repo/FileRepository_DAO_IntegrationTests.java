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
import org.mockito.Mock;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.github.dergil.hierarchynote.model.dao.FileDao;
import com.github.dergil.hierarchynote.model.entity.FileEntity;
import com.github.dergil.hierarchynote.model.network.FileAPI;
import com.github.dergil.hierarchynote.model.repository.FileRepository;
import com.github.dergil.hierarchynote.model.db.FileRoomDatabase;
import com.github.dergil.hierarchynote.util.LiveDataTestUtil;
import com.github.dergil.hierarchynote.util.TestUtil;

import java.util.Collection;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class FileRepository_DAO_IntegrationTests {

    private TestUtil testUtil = new TestUtil();
//    @Mock
//    private FileAPI networking;
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
        FileEntity readNote = fileDao.find(findCurrentId(note));
        assertTrue(testUtil.compareNotesWithoutId(note, readNote));
    }

    private Long findCurrentId(FileEntity insertedFile) {
        System.out.println(fileDao.findAll());
        return findByText(fileDao.findAll(), insertedFile.getText()).getId();
    }

    @Test
    public void readAll() throws InterruptedException {
        repository.insert(note);
        repository.insert(note2);
        Thread.sleep(100);
//        LiveData<List<FileEntity>> liveNotes = repository.getAllFiles(note.getDirectory_name());
//        List<FileEntity> readNotes = LiveDataTestUtil.getValue(liveNotes);
        List<FileEntity> readNotes = fileDao.findAll();
//        assertTrue(compareNotes(note, readNotes.get(0)));
        Long note1Id = findCurrentId(note);
        Long note2Id = findCurrentId(note2);
        note.setId(note1Id);
        note2.setId(note2Id);
        System.out.println(readNotes);
        System.out.println(note);
        System.out.println(note2);
        assertTrue(findById(readNotes, note1Id) != null);
        assertTrue(findById(readNotes, note2Id) != null);
//        assertTrue(readNotes.contains(note));
//        assertTrue(readNotes.contains(note2));
//        assertTrue(compareNotes(note2, readNotes.get(1)));

    }

    private FileEntity findById(Collection<FileEntity> list, Long id) {
        return list.stream().filter(carnet -> id.equals(carnet.getId())).findFirst().orElse(null);
    }


    @Test
    public void updateNote() throws InterruptedException {
        String newText = "new Text";
        repository.insert(note);
        Thread.sleep(250);
        note.setId(findCurrentId(note));
        note.setText(newText);
        repository.update(note);
        Thread.sleep(250);
        List<FileEntity> readFiles = fileDao.findAll();
        FileEntity file = findByText(readFiles, newText);
//        FileEntity readNote = fileDao.find(note.getId());
        assertTrue(testUtil.compareNotesWithoutId(note, file));
    }

    private FileEntity findByText(Collection<FileEntity> list, String text) {
        return list.stream().filter(carnet -> text.equals(carnet.getText())).findFirst().orElse(null);
    }

    @Test
    public void deleteNote() throws InterruptedException {
        repository.insert(note);
        Thread.sleep(500);
        note.setId(findCurrentId(note));
        repository.delete(note.getId());
        Thread.sleep(500);
        assertNull(fileDao.find(note.getId()));
    }
}
