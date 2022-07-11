package com.github.dergil.hierarchynote;

import android.app.Application;

import com.github.dergil.hierarchynote.model.db.NoteRoomDatabase;
import com.github.dergil.hierarchynote.model.network.NoteAPI;
import com.github.dergil.hierarchynote.model.repository.NoteRepository;

public class HierarchyApp extends Application {

    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public NoteRoomDatabase getDatabase() {
        return NoteRoomDatabase.getInstance(this, mAppExecutors);
    }

    public NoteRepository getRepository() {
        return NoteRepository.getInstance(getDatabase(), new NoteAPI());
    }
}
