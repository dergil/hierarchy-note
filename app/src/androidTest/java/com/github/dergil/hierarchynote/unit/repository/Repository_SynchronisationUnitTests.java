package com.github.dergil.hierarchynote.unit.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.dergil.hierarchynote.model.dao.NoteDao;
import com.github.dergil.hierarchynote.model.db.NoteRoomDatabase;
import com.github.dergil.hierarchynote.model.dto.ResponseDto;
import com.github.dergil.hierarchynote.model.dto.UpdateFileDto;
import com.github.dergil.hierarchynote.model.entity.NoteEntity;
import com.github.dergil.hierarchynote.model.network.NoteAPI;
import com.github.dergil.hierarchynote.model.repository.NoteRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class Repository_SynchronisationUnitTests {
    @Mock
    private NoteAPI networking;
    @Mock
    private NoteDao noteDao;
    @Mock
    NoteRoomDatabase database;

    NoteEntity note = new NoteEntity("name", "text", "MYDIR", false, false);


    @Before
    public void setUp() throws Exception {
        when(database.wordDao()).thenReturn(noteDao);
        when(noteDao.getAlphabetizedWords("MYDIR")).thenReturn(null);
        when(noteDao.findAll()).thenReturn(new ArrayList<NoteEntity>());
    }

//    @Test
//    public void attemptSyncAtStartup() throws InterruptedException {
//        NoteRepository repository = new NoteRepository(database, networking);
//        Thread.sleep(5);
//        Mockito.verify(networking).getNotes();
//    }
//
//    @Test
//    public void createNoteInNetwork() throws Throwable {
//        NoteRepository repository = new NoteRepository(database, networking);
//        repository.insert(note);
//        Thread.sleep(50);
//        Mockito.verify(networking).insert(note);
//    }
//
//    private void insertNote(NoteRepository repository) throws InterruptedException {
//        repository.insert(note);
//        Thread.sleep(250);
//    }

//    @Test
//    public void updateNoteInNetwork_changeName() throws Throwable {
//        String newName = "new name";
//        NoteRepository repository = new NoteRepository(database, networking);
//        Thread.sleep(200);
//        insertNote(repository);
//        note.setName(newName);
//
//        repository.update(note);
//        Thread.sleep(200);
//
//        ArgumentCaptor<UpdateFileDto> dtoCaptor = ArgumentCaptor.forClass(UpdateFileDto.class);
//        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
//        Mockito.verify(networking).update(longCaptor.capture(), dtoCaptor.capture());
//        UpdateFileDto capturedArgument = dtoCaptor.getValue();
//        assertEquals(capturedArgument.getOp(), "replace");
//        assertEquals(capturedArgument.getPath(), "/name");
//        assertEquals(capturedArgument.getValue(), newName);
//    }



//    The server has files that are not saved locally
    @Test
    public void dbSync_insertsFilesFromServer() throws InterruptedException {
        NoteEntity note1 = new NoteEntity("name", "text", "MYDIR", false, false);
        NoteEntity note2 = new NoteEntity("name2", "text2", "MYDIR", false, false);
        note1.setId(1L);
        note2.setId(2L);
        List<NoteEntity> remoteFiles = new ArrayList<>();
        remoteFiles.add(note1);
        remoteFiles.add(note2);
        when(networking.getNotes()).thenReturn(remoteFiles);

        NoteRepository repository = new NoteRepository(database, networking);
        Thread.sleep(50);

        note1.setSynced(Boolean.TRUE);
        verify(noteDao).insert(note1);
        note2.setSynced(Boolean.TRUE);
        verify(noteDao).insert(note2);
    }

    @Test
    public void dbSync_uploadsNonSyncedLocalFiles() throws InterruptedException {
        NoteEntity note1 = new NoteEntity("name", "text", "MYDIR", false, false);
        NoteEntity note2 = new NoteEntity("name2", "text2", "MYDIR", false, false);
        note1.setId(1L);
        note2.setId(2L);
        List<NoteEntity> localFiles = new ArrayList<>();
        localFiles.add(note1);
        localFiles.add(note2);
        when(noteDao.findAll()).thenReturn(localFiles);
//        when(networking.getNotes()).thenReturn(null);
//        ResponseDto responseDto = new ResponseDto();
//        responseDto.setId(42L);
//        when(networking.insert(any())).thenReturn(responseDto);

        NoteRepository repository = new NoteRepository(database, networking);
        Thread.sleep(50);

        verify(networking).insert(note1);
        verify(networking).insert(note2);
    }

    @Test
    public void dbSync_changesLocalIdToRemoteId() throws InterruptedException {
        Long remoteId = 42L;
        NoteEntity note1 = new NoteEntity("name", "text", "MYDIR", false, false);
        note1.setId(34L);
        List<NoteEntity> localFiles = new ArrayList<>();
        localFiles.add(note1);
        when(noteDao.findAll()).thenReturn(localFiles);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setId(remoteId);
        when(networking.insert(any())).thenReturn(responseDto);

        NoteRepository repository = new NoteRepository(database, networking);
        Thread.sleep(50);

        note1.setId(remoteId);
        note1.setSynced(Boolean.TRUE);
        verify(noteDao).update(note1);
    }

//    private void insertNote() throws InterruptedException {
//        note.setId(id);
//        repository.insert(note);
//        Thread.sleep(50);
//    }
}
