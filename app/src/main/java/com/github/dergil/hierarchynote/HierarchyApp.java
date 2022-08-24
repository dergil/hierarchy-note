package com.github.dergil.hierarchynote;

import android.app.Application;

import com.github.dergil.hierarchynote.model.db.FileRoomDatabase;
import com.github.dergil.hierarchynote.model.network.FileAPI;
import com.github.dergil.hierarchynote.model.repository.FileRepository;
import com.github.dergil.hierarchynote.view.activities.data.LoginDataSource;
import com.github.dergil.hierarchynote.view.activities.data.LoginRepository;

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

    public FileRepository getFileRepository() {
        return FileRepository.getInstance(getDatabase(), new FileAPI());
    }

    public LoginRepository getLoginRepository() {
        return LoginRepository.getInstance(getDatabase(), new LoginDataSource());
    }
}
