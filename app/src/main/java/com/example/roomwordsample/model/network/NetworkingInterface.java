package com.example.roomwordsample.model.network;

import com.example.roomwordsample.model.entity.NoteEntity;
import com.example.roomwordsample.model.dto.ResponseDto;
import com.example.roomwordsample.model.dto.UpdateFileDto;

import java.util.List;

public interface NetworkingInterface {
    ResponseDto insert(NoteEntity note);
    boolean update(Long id, UpdateFileDto updateFileDto);
    void delete(Long id);
    List<NoteEntity> getNotes();
}
