package com.example.roomwordsample;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class NoteRepository {

    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllWords;
    public static final String DIRECTORY_NAME = "MYDIR";


    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.wordDao();
        mAllWords = mNoteDao.getAlphabetizedWords(DIRECTORY_NAME);
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Note>> getAllWords(String directory_name) {
        mAllWords = mNoteDao.getAlphabetizedWords(directory_name);
        return mAllWords;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Note note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.insert(note);
        });
    }

    void update(Note note){
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.update(note);
        });
    }

    void delete(String name){
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.deleteById(name);
        });
    }
}

