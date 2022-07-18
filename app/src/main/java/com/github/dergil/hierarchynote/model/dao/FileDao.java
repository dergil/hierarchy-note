package com.github.dergil.hierarchynote.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.github.dergil.hierarchynote.model.entity.FileEntity;

import java.util.List;

@Dao
public interface FileDao {

@Insert(onConflict = OnConflictStrategy.IGNORE)
Long insert(FileEntity note);

@Query("SELECT * FROM file_table WHERE id = :id")
FileEntity find(Long id);

@Query("SELECT * FROM file_table")
List<FileEntity> findAll();

@Update
void update(FileEntity note);

@Query("DELETE FROM file_table WHERE id = :id")
void deleteById(Long id);

@Query("DELETE FROM file_table")
void deleteAll();

@Query("SELECT * FROM file_table WHERE directory_name = :directory_name ORDER BY name ASC")
LiveData<List<FileEntity>> getAlphabetizedWords(String directory_name);
}

