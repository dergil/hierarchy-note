package com.github.dergil.hierarchynote;

import android.app.Application;

import com.github.dergil.hierarchynote.model.db.FileRoomDatabase;
import com.github.dergil.hierarchynote.model.network.FileAPI;
import com.github.dergil.hierarchynote.model.repository.FileRepository;

public class HierarchyApp extends Application {

    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public FileRoomDatabase getDatabase() {
        return FileRoomDatabase.getInstance(this, mAppExecutors);
    }

    public FileRepository getRepository() {
        return FileRepository.getInstance(getDatabase(), new FileAPI());
    }
}
