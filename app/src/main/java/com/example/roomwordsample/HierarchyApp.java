package com.example.roomwordsample;

import android.app.Application;

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
        return NoteRepository.getInstance(getDatabase(), new Networking());
    }
}
