package com.github.dergil.hierarchynote.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.github.dergil.hierarchynote.HierarchyApp;
import com.github.dergil.hierarchynote.model.entity.FileEntity;
import com.github.dergil.hierarchynote.model.repository.FileRepository;
import com.github.dergil.hierarchynote.model.repository.FileRepositoryInterface;

import java.util.List;

public class FileViewModel extends AndroidViewModel implements FileViewModelInterface {

    private FileRepositoryInterface mRepository;

    public FileViewModel(@NonNull Application application, FileRepositoryInterface fileRepository) {
        super(application);
        mRepository = fileRepository;
    }

    public LiveData<List<FileEntity>> getAllFiles(String directory_name) {
        return mRepository.getAllFiles(directory_name);
    }

    public void insert(FileEntity file) { mRepository.insert(file); }

    public void update(FileEntity file) { mRepository.update(file);}

    public void deleteById(Long id) { mRepository.delete(id);}

    /**
     * A creator is used to inject the product ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the product ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final FileRepository mRepository;

        public Factory(@NonNull Application application) {
            mApplication = application;
            mRepository = ((HierarchyApp) application).getFileRepository();
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new FileViewModel(mApplication, mRepository);
        }
    }
}

