package com.github.dergil.hierarchynote.viewmodel;

import androidx.lifecycle.LiveData;

import com.github.dergil.hierarchynote.model.entity.FileEntity;

import java.util.List;

public interface FileViewModelInterface {
    LiveData<List<FileEntity>> getAllFiles(String directory_name);

    void insert(FileEntity file);

    void update(FileEntity file);

    void deleteById(Long id);
}
