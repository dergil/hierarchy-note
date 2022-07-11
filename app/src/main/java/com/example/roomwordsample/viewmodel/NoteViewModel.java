package com.example.roomwordsample.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.roomwordsample.HierarchyApp;
import com.example.roomwordsample.model.network.Networking;
import com.example.roomwordsample.model.entity.NoteEntity;
import com.example.roomwordsample.model.repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel implements NoteViewModelInterface {
    public static final String DIRECTORY_NAME = "MYDIR";

    private NoteRepository mRepository;

    private LiveData<List<NoteEntity>> mAllWords;

    private Networking networking;

    public NoteViewModel(@NonNull Application application, NoteRepository noteRepository) {
        super(application);
//        mRepository = new NoteRepository(application, new Networking());
//        mRepository = noteRepository;
//        mRepository = ((HierarchyApp) application).getRepository();
        mRepository = noteRepository;
        mAllWords = noteRepository.getAllWords(DIRECTORY_NAME);
//        this.networking = networking;
    }

    public LiveData<List<NoteEntity>> getAllWords(String directory_name) {
        return mRepository.getAllWords(directory_name);
    }

    public void insert(NoteEntity note) { mRepository.insert(note); }

    public void update(NoteEntity note) { mRepository.update(note);}

    public void deleteById(Long id) { mRepository.delete(id);}

    //    public Note getParentDir(String directory_name) {
//        mRepository.getParentDir(directory_name);
//        Note note = mRepository.parentDir;
//        return note;
//    }

    /**
     * A creator is used to inject the product ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the product ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

//        private final Networking networking;


        private final NoteRepository mRepository;

        public Factory(@NonNull Application application) {
            mApplication = application;
            mRepository = ((HierarchyApp) application).getRepository();
//            this.networking = networking;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new NoteViewModel(mApplication, mRepository);
        }
    }
}

