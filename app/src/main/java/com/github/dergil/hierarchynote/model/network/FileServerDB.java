package com.github.dergil.hierarchynote.model.network;

import com.github.dergil.hierarchynote.model.entity.FileEntity;
import com.github.dergil.hierarchynote.model.dto.ResponseDto;
import com.github.dergil.hierarchynote.model.dto.UpdateFileDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface FileServerDB {

    @POST("/api/core/file/create")
    Call<ResponseDto> saveFile(@Body FileEntity file);

    @GET("/api/core/file/find-all")
    Call<List<FileEntity>> getFiles();

    @PUT("/api/core/file/update")
    Call<ResponseDto> editFile(@Query("id") Long id, @Body List<UpdateFileDto> updateFileDto);

    @DELETE("/api/core/file/delete")
    Call<ResponseDto> deleteFile(@Query("id") Long id);


}
