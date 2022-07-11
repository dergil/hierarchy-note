package com.example.roomwordsample.model.repository;

import androidx.lifecycle.LiveData;

import com.example.roomwordsample.model.entity.NoteEntity;

import java.util.List;

public interface NoteRepositoryInterface {
    LiveData<List<NoteEntity>> getAllWords(String directory_name);
    void insert(NoteEntity note) ;
    void update(NoteEntity note);
    void delete (Long id);
}
