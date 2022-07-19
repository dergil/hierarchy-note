package com.github.dergil.hierarchynote.local_tests.unit.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.dergil.hierarchynote.model.dao.FileDao;
import com.github.dergil.hierarchynote.model.db.FileRoomDatabase;
import com.github.dergil.hierarchynote.model.dto.ResponseDto;
import com.github.dergil.hierarchynote.model.entity.FileEntity;
import com.github.dergil.hierarchynote.model.network.FileAPI;
import com.github.dergil.hierarchynote.model.repository.FileRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class Repository_SynchronisationUnitTests {
    @Mock
    private FileAPI networking;
    @Mock
    private FileDao fileDao;
    @Mock
    FileRoomDatabase database;

    @Before
    public void setUp() throws Exception {
        when(database.fileDao()).thenReturn(fileDao);
        when(fileDao.getAlphabetizedFiles("MYDIR")).thenReturn(null);
        when(fileDao.findAll()).thenReturn(new ArrayList<FileEntity>());
    }

//    The server has files that are not saved locally
    @Test
    public void dbSync_insertsFilesFromServer() throws InterruptedException {
        FileEntity note1 = new FileEntity("name", "text", "MYDIR", false, false);
        FileEntity note2 = new FileEntity("name2", "text2", "MYDIR", false, false);
        note1.setId(1L);
        note2.setId(2L);
        List<FileEntity> remoteFiles = new ArrayList<>();
        remoteFiles.add(note1);
        remoteFiles.add(note2);
        when(networking.getNotes()).thenReturn(remoteFiles);

        FileRepository repository = new FileRepository(database, networking);
        Thread.sleep(50);

        note1.setSynced(Boolean.TRUE);
        verify(fileDao).insert(note1);
        note2.setSynced(Boolean.TRUE);
        verify(fileDao).insert(note2);
    }

    @Test
    public void dbSync_uploadsNonSyncedLocalFiles() throws InterruptedException {
        FileEntity note1 = new FileEntity("name", "text", "MYDIR", false, false);
        FileEntity note2 = new FileEntity("name2", "text2", "MYDIR", false, false);
        note1.setId(1L);
        note2.setId(2L);
        List<FileEntity> localFiles = new ArrayList<>();
        localFiles.add(note1);
        localFiles.add(note2);
        when(fileDao.findAll()).thenReturn(localFiles);
        FileRepository repository = new FileRepository(database, networking);
        Thread.sleep(50);

        verify(networking).insert(note1);
        verify(networking).insert(note2);
    }

    @Test
    public void dbSync_changesLocalIdToRemoteId() throws InterruptedException {
        Long remoteId = 42L;
        FileEntity note1 = new FileEntity("name", "text", "MYDIR", false, false);
        note1.setId(34L);
        List<FileEntity> localFiles = new ArrayList<>();
        localFiles.add(note1);
        when(fileDao.findAll()).thenReturn(localFiles);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setId(remoteId);
        when(networking.insert(any())).thenReturn(responseDto);

        FileRepository repository = new FileRepository(database, networking);
        Thread.sleep(50);

        note1.setId(remoteId);
        note1.setSynced(Boolean.TRUE);
        verify(fileDao).update(note1);
    }
}
