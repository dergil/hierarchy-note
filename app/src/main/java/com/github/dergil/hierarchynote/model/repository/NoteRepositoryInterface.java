package com.github.dergil.hierarchynote.model.repository;

import androidx.lifecycle.LiveData;

import com.github.dergil.hierarchynote.model.entity.NoteEntity;

import java.util.List;

public interface NoteRepositoryInterface {
    LiveData<List<NoteEntity>> getAllWords(String directory_name);
    void insert(NoteEntity note) ;
    void update(NoteEntity note);
    void delete (Long id);
}
