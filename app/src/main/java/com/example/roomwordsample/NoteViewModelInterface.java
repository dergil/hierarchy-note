package com.example.roomwordsample;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface NoteViewModelInterface {
    LiveData<List<Note>> getAllWords(String directory_name);

    void insert(Note note);

    void update(Note note);

    void deleteById(Long id);
}
