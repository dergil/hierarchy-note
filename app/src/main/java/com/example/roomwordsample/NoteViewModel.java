package com.example.roomwordsample;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    public static final String DIRECTORY_NAME = "MYDIR";


    private NoteRepository mRepository;

    private LiveData<List<Note>> mAllWords;

    public NoteViewModel(Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mAllWords = mRepository.getAllWords(DIRECTORY_NAME);
    }

    LiveData<List<Note>> getAllWords(String directory_name) {
        mAllWords = mRepository.getAllWords(directory_name);
        return mAllWords; }

    public void insert(Note note) { mRepository.insert(note); }

    public void update(Note note) { mRepository.update(note);}

    public void deleteById(String name) { mRepository.delete(name);}
}

