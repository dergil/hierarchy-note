package com.github.dergil.hierarchynote.local_tests.unit.model;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.dergil.hierarchynote.model.db.FileRoomDatabase;
import com.github.dergil.hierarchynote.model.entity.FileEntity;
import com.github.dergil.hierarchynote.model.dao.FileDao;
import com.github.dergil.hierarchynote.local_tests.util.TestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import javax.inject.Inject;

@RunWith(AndroidJUnit4.class)
public class DaoAndDb {
    private FileDao fileDao;
    private FileRoomDatabase db;

    @Inject
    TestUtil testUtil = new TestUtil();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FileRoomDatabase.class).build();
        fileDao = db.fileDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void readFile() throws Exception {
        FileEntity createdNote = new FileEntity("name", "text", "MYDIR", false, false);
        Long id = 4242L;
        createdNote.setId(id);
        fileDao.insert(createdNote);
        FileEntity foundNote = fileDao.find(id);
        assertTrue(TestUtil.compareNotes(createdNote, foundNote));
    }

    @Test
    public void deleteFile() {
        FileEntity insertedNote = insertNote1();
        fileDao.deleteById(insertedNote.getId());
        FileEntity foundNote = fileDao.find(insertedNote.getId());
        assertNull(foundNote);
    }

    @Test
    public void updateFile() {
        String newText = "changed Text";
        FileEntity insertedNote = insertNote1();
        insertedNote.setText(newText);
        fileDao.update(insertedNote);
        FileEntity updatedNote = fileDao.find(insertedNote.getId());
        assertTrue(TestUtil.compareNotes(insertedNote, updatedNote));
    }

    @Test
    public void findAll() {
        FileEntity insertedNote1 = insertNote1();
        FileEntity insertedNote2 = insertNote2();
        List<FileEntity> allNotes = fileDao.findAll();
        assertTrue(TestUtil.compareNotes(insertedNote1, allNotes.get(0)));
        assertTrue(TestUtil.compareNotes(insertedNote2, allNotes.get(1)));
    }

    @Test
    public void deleteAll() {
        insertNote1();
        insertNote2();
        fileDao.deleteAll();
        assertTrue(fileDao.findAll().isEmpty());
    }

    private FileEntity insertNote1(){
        FileEntity note = testUtil.createNote1();
        fileDao.insert(note);
        return note;
    }

    private FileEntity insertNote2(){
        FileEntity note = testUtil.createNote2();
        fileDao.insert(note);
        return note;
    }
}

