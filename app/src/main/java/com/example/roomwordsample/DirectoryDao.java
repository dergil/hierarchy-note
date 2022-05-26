package com.example.roomwordsample;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface DirectoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Directory directory);
}
