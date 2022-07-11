package com.example.roomwordsample.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.roomwordsample.model.entity.NoteEntity;

import java.util.List;

@Dao
public interface NoteDao {

@Insert(onConflict = OnConflictStrategy.IGNORE)
Long insert(NoteEntity note);

@Query("SELECT * FROM note_table WHERE id = :id")
NoteEntity find(Long id);

@Query("SELECT * FROM note_table")
List<NoteEntity> findAll();

@Update
void update(NoteEntity note);

@Query("DELETE FROM note_table WHERE id = :id")
void deleteById(Long id);

@Query("DELETE FROM note_table")
void deleteAll();

@Query("SELECT * FROM note_table WHERE directory_name = :directory_name ORDER BY name ASC")
LiveData<List<NoteEntity>> getAlphabetizedWords(String directory_name);

//@Query("SELECT * FROM note_table WHERE name = :directory_name")
//Note getParentDir(String directory_name);
}

