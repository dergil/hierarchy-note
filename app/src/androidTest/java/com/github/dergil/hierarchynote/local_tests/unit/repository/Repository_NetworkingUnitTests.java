package com.github.dergil.hierarchynote.local_tests.unit.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.github.dergil.hierarchynote.model.entity.FileEntity;
import com.github.dergil.hierarchynote.model.network.FileAPI;
import com.github.dergil.hierarchynote.model.repository.FileRepository;
import com.github.dergil.hierarchynote.model.db.FileRoomDatabase;
import com.github.dergil.hierarchynote.model.dto.UpdateFileDto;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class Repository_NetworkingUnitTests {
    private FileRoomDatabase db;
    private FileRepository repository;
    private FileEntity note = new FileEntity("name", "text", "MYDIR", false, false);
    private Long id = 4242L;

    @Mock
    private FileAPI networking;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FileRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        repository = new FileRepository(db, networking);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void attemptSyncAtStartup() throws InterruptedException {
        Thread.sleep(50);
        Mockito.verify(networking).getNotes();
    }

    @Test
    public void createNoteInNetwork() throws Throwable {
        insertNote();
        Mockito.verify(networking).insert(note);
    }

    @Test
    public void updateNoteInNetwork_changeName() throws Throwable {
        String newName = "new name";
        insertNote();
        note.setName(newName);

        repository.update(note);
        Thread.sleep(5);


        ArgumentCaptor<UpdateFileDto> dtoCaptor = ArgumentCaptor.forClass(UpdateFileDto.class);
        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(networking).update(longCaptor.capture(), dtoCaptor.capture());
        UpdateFileDto capturedArgument = dtoCaptor.getValue();
        assertEquals(capturedArgument.getOp(), "replace");
        assertEquals(capturedArgument.getPath(), "/name");
        assertEquals(capturedArgument.getValue(), newName);
    }

    @Test
    public void updateNoteInNetwork_changeText() throws Throwable {
        String newText = "new text";
        insertNote();
        note.setText(newText);

        repository.update(note);
        Thread.sleep(5);

        ArgumentCaptor<UpdateFileDto> dtoCaptor = ArgumentCaptor.forClass(UpdateFileDto.class);
        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(networking).update(longCaptor.capture(), dtoCaptor.capture());
        UpdateFileDto capturedArgument = dtoCaptor.getValue();
        assertEquals(capturedArgument.getOp(), "replace");
        assertEquals(capturedArgument.getPath(), "/text");
        assertEquals(capturedArgument.getValue(), newText);
    }

    @Test
    public void deleteNoteInNetwork() throws InterruptedException {
        repository.insert(note);
        Thread.sleep(50);

        repository.delete(id);
        Thread.sleep(5);
        Mockito.verify(networking).delete(id);
    }

    private void insertNote() throws InterruptedException {
        note.setId(id);
        repository.insert(note);
        Thread.sleep(50);
    }
}
