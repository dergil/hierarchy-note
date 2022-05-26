package com.example.roomwordsample;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

@Insert(onConflict = OnConflictStrategy.IGNORE)
Long insert(Note note);

@Query("SELECT * FROM note_table WHERE id = :id")
Note find(Long id);

@Update
void update(Note note);

@Query("DELETE FROM note_table WHERE id = :id")
void deleteById(Long id);

@Query("DELETE FROM note_table")
void deleteAll();

@Query("SELECT * FROM note_table WHERE directory_name = :directory_name ORDER BY name ASC")
LiveData<List<Note>> getAlphabetizedWords(String directory_name);
}

