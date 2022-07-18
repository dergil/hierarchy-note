package com.github.dergil.hierarchynote.model.repository;

import androidx.lifecycle.LiveData;

import com.github.dergil.hierarchynote.model.dao.FileDao;
import com.github.dergil.hierarchynote.model.db.FileRoomDatabase;
import com.github.dergil.hierarchynote.model.entity.FileEntity;
import com.github.dergil.hierarchynote.model.network.FileAPI;
import com.github.dergil.hierarchynote.model.network.ServerDB;
import com.github.dergil.hierarchynote.model.dto.ResponseDto;
import com.github.dergil.hierarchynote.model.dto.UpdateFileDto;

import java.util.List;


public class FileRepository implements FileRepositoryInterface {

    private static FileRepository sInstance;
    private FileRoomDatabase mDatabase;
    private FileDao mFileDao;
    private LiveData<List<FileEntity>> mAllFiles;
    FileAPI networking;
    public static String DIRECTORY_NAME = "MYDIR";

    static String BASE_URL = "http://10.0.2.2:8080/";

    public FileRepository(FileRoomDatabase database, FileAPI networking) {
        mDatabase = database;
        mFileDao = database.fileDao();
        mAllFiles = mFileDao.getAlphabetizedWords(DIRECTORY_NAME);
        this.networking = networking;
        start();
    }

    public static FileRepository getInstance(final FileRoomDatabase database, FileAPI networking) {
        if (sInstance == null) {
            synchronized (FileRepository.class) {
                if (sInstance == null) {
                    sInstance = new FileRepository(database, networking);
                }
            }
        }
        return sInstance;
    }

    private void start() {
        FileRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<FileEntity> remoteNotes = networking.getNotes();
            List<FileEntity> localFiles = mFileDao.findAll();
            if (remoteNotes != null) {
                for (FileEntity remoteNote : remoteNotes) {
                    if (!localFiles.contains(remoteNote)){
                        remoteNote.setSynced(Boolean.TRUE);
                        mFileDao.insert(remoteNote);
                    }
                }
            }
            sync();
        });
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<FileEntity>> getAllFiles(String directory_name) {
        mAllFiles = mFileDao.getAlphabetizedWords(directory_name);
        return mAllFiles;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(FileEntity note) {
        FileRoomDatabase.databaseWriteExecutor.execute(() -> {
            ResponseDto response = networking.insert(note);
            if (response != null){
                note.setId(response.getId());
                note.setSynced(Boolean.TRUE);
            }
            mFileDao.insert(note);
        });
    }

    private void sync() {
        List<FileEntity> localFiles = mFileDao.findAll();
        Long id = null;
        for (FileEntity localFile : localFiles) {
            if (!localFile.getSynced()) {
                id = insertRemotely(localFile);
            }
            if (id != null){
                localFile.setId(id);
                localFile.setSynced(Boolean.TRUE);
                mFileDao.update(localFile);
            }
        }
    }
    private Long insertRemotely(FileEntity note) {
        ResponseDto response = networking.insert(note);
        if (response != null)
            return response.getId();
        else return null;
    }

    public void update(FileEntity note){
        FileRoomDatabase.databaseWriteExecutor.execute(() -> {
            Long id = note.getId();
            FileEntity oldNote = mFileDao.find(id);
            if (!oldNote.getName().equals(note.getName())) {
                networking.update(id, new UpdateFileDto("replace", "/name", note.getName()));
            }
            if (!oldNote.getText().equals(note.getText())) {
                networking.update(id, new UpdateFileDto("replace", "/text", note.getText()));
            }
            mFileDao.update(note);
        });
    }

    public void delete(Long id){
        FileRoomDatabase.databaseWriteExecutor.execute(() -> {
            mFileDao.deleteById(id);
            networking.delete(id);
        });
    }
}

