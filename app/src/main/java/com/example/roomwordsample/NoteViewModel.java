package com.example.roomwordsample;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.HashSet;
import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    public static final String DIRECTORY_NAME = "MYDIR";


    private NoteRepository mRepository;

    private LiveData<List<Note>> mAllWords;

    public NoteViewModel(Application application) {
        super(application);
        mRepository = new NoteRepository(application);
//        mAllWords = mRepository.getAllWords(DIRECTORY_NAME);
    }

//    public Note getParentDir(String directory_name) {
//        mRepository.getParentDir(directory_name);
//        Note note = mRepository.parentDir;
//        return note;
//    }

    LiveData<List<Note>> getAllWords(String directory_name) {
        mAllWords = mRepository.getAllWords(directory_name);
        return mAllWords; }

    public void insert(Note note) { mRepository.insert(note); }

    public void update(Note note) { mRepository.update(note);}

    public void deleteById(Long id) { mRepository.delete(id);}
}

