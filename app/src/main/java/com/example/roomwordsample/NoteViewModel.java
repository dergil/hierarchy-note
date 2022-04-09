package com.example.roomwordsample;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository mRepository;

    private final LiveData<List<Note>> mAllWords;

    public NoteViewModel(Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    LiveData<List<Note>> getAllWords() { return mAllWords; }

    public void insert(Note note) { mRepository.insert(note); }

    public void update(Note note) { mRepository.update(note);}

    public void deleteById(String name) { mRepository.delete(name);}
}

