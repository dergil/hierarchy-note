package com.example.roomwordsample.model.network;

import com.example.roomwordsample.model.entity.NoteEntity;
import com.example.roomwordsample.model.dto.ResponseDto;
import com.example.roomwordsample.model.dto.UpdateFileDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ServerDB {

    @POST("/api/core/file/create")
    Call<ResponseDto> saveNote(@Body NoteEntity file);

    @GET("/api/core/file/find-all")
    Call<List<NoteEntity>> getNotes();

    @PUT("/api/core/file/update")
    Call<ResponseDto> editNote(@Query("id") Long id, @Body List<UpdateFileDto> updateFileDto);

    @DELETE("/api/core/file/delete")
    Call<ResponseDto> deleteNote(@Query("id") Long id);







//    public List<Note> getAllWords(String directory_name);
//
//    public void insert(Note note);
//
//    public void update(Note note);
//
//    public void deleteById(long id);
}
