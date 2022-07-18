package com.github.dergil.hierarchynote.model.network;

import com.github.dergil.hierarchynote.model.entity.FileEntity;
import com.github.dergil.hierarchynote.model.dto.ResponseDto;
import com.github.dergil.hierarchynote.model.dto.UpdateFileDto;

import java.util.List;

public interface NetworkingInterface {
    ResponseDto insert(FileEntity note);
    boolean update(Long id, UpdateFileDto updateFileDto);
    void delete(Long id);
    List<FileEntity> getNotes();
}
