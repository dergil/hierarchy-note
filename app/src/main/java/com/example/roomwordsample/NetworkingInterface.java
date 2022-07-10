package com.example.roomwordsample;

import java.util.List;

public interface NetworkingInterface {
    ResponseDto insert(Note note);
    boolean update(Long id, UpdateFileDto updateFileDto);
    void delete(Long id);
    List<Note> getNotes();
}
