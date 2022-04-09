package com.example.roomwordsample;

import android.app.Application;

public class DirectoryRepository {
    private DirectoryDao directoryDao;

    DirectoryRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        directoryDao = db.directoryDao();
    }

    void insert(Directory directory) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            directoryDao.insert(directory);
        });
    }
}
