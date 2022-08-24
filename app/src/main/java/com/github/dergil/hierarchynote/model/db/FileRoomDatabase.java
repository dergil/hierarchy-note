package com.github.dergil.hierarchynote.model.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.github.dergil.hierarchynote.AppExecutors;
import com.github.dergil.hierarchynote.model.dao.JwtDao;
import com.github.dergil.hierarchynote.model.entity.FileEntity;
import com.github.dergil.hierarchynote.model.dao.FileDao;
import com.github.dergil.hierarchynote.model.entity.JwtEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FileEntity.class, JwtEntity.class}, version = 45, exportSchema = false)
public abstract class FileRoomDatabase extends RoomDatabase {

    public abstract FileDao fileDao();
    public abstract JwtDao jwtDao();

    @VisibleForTesting
    public static final String DATABASE_NAME = "hierarchy-file-db";

    private static volatile FileRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static FileRoomDatabase getInstance(final Context context, final AppExecutors executors) {
        if (INSTANCE == null) {
            synchronized (FileRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context.getApplicationContext(), executors);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static FileRoomDatabase buildDatabase(final Context appContext,
                                                  final AppExecutors executors) {
        return Room.databaseBuilder(appContext, FileRoomDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(() -> {
                            FileRoomDatabase database = FileRoomDatabase.getInstance(appContext, executors);
                        });
                    }
                })
                .fallbackToDestructiveMigration()
                .build();
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                FileDao dao = INSTANCE.fileDao();
                dao.deleteAll();
            });
        }
    };

}

