package com.github.dergil.hierarchynote.model.network;

import com.github.dergil.hierarchynote.model.entity.FileEntity;
import com.github.dergil.hierarchynote.model.dto.ResponseDto;
import com.github.dergil.hierarchynote.model.dto.UpdateFileDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FileAPI implements NetworkingInterface{
    ServerDB serverDB;
    String BASE_URL = "http://10.0.2.2:8080/";


    public FileAPI() {
        initNetworkingStack();
    }

    public ResponseDto insert(FileEntity note) {
        Call<ResponseDto> questions = serverDB.saveFile(note);
        try {
            Response<ResponseDto> execute = questions.execute();
            if (execute.isSuccessful()){
                return execute.body();
            } else return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean update(Long id, UpdateFileDto updateFileDto){
        List<UpdateFileDto> updateBody = new ArrayList<>();
        updateBody.add(updateFileDto);
        Call<ResponseDto> questions = serverDB.editFile(id, updateBody);
        try {
            Response<ResponseDto> execute = questions.execute();
            if (execute.isSuccessful())
                return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void delete(Long id) {
        Call<ResponseDto> questions = serverDB.deleteFile(id);
        try {
            Response<ResponseDto> execute = questions.execute();
            System.out.println(execute.isSuccessful());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<FileEntity> getNotes(){
        Call<List<FileEntity>> questions = serverDB.getFiles();
        try {
            Response<List<FileEntity>> execute = questions.execute();
            List<FileEntity> body = execute.body();
            if (execute.isSuccessful()){
                return body;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void initNetworkingStack(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        serverDB = retrofit.create(ServerDB.class);
    }
}
