package com.github.dergil.hierarchynote.model.repository;

import androidx.lifecycle.LiveData;

import com.github.dergil.hierarchynote.model.entity.FileEntity;

import java.util.List;

public interface FileRepositoryInterface {
    LiveData<List<FileEntity>> getAllFiles(String directory_name);
    void insert(FileEntity note) ;
    void update(FileEntity note);
    void delete (Long id);
}
