package com.example.roomwordsample.viewmodel;

import androidx.lifecycle.LiveData;

import com.example.roomwordsample.model.entity.NoteEntity;

import java.util.List;

public interface NoteViewModelInterface {
    LiveData<List<NoteEntity>> getAllWords(String directory_name);

    void insert(NoteEntity note);

    void update(NoteEntity note);

    void deleteById(Long id);
}
