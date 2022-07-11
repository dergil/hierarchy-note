package com.github.dergil.hierarchynote.model.network;

import com.github.dergil.hierarchynote.model.entity.NoteEntity;
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

public class NoteAPI implements NetworkingInterface{
    ServerDB serverDB;
    String BASE_URL = "http://10.0.2.2:8080/";


    public NoteAPI() {
        initNetworkingStack();
    }

    public ResponseDto insert(NoteEntity note) {
        Call<ResponseDto> questions = serverDB.saveNote(note);
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
        Call<ResponseDto> questions = serverDB.editNote(id, updateBody);
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
        Call<ResponseDto> questions = serverDB.deleteNote(id);
        try {
            Response<ResponseDto> execute = questions.execute();
            System.out.println(execute.isSuccessful());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


        public List<NoteEntity> getNotes(){
        Call<List<NoteEntity>> questions = serverDB.getNotes();
        try {
            Response<List<NoteEntity>> execute = questions.execute();
            List<NoteEntity> body = execute.body();
            if (execute.isSuccessful()){
                return body;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }



//        final List<Note>[] remoteNotes = (List<Note>[]) new Object[1];
//        Call<List<Note>> questions1 = serverDB.getNotes();
//
//        questions1.enqueue(new Callback<List<Note>>() {
//            @Override
//            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
//                System.out.println(response.body());
//                System.out.println(response.isSuccessful());
////                remoteNotes.addAll(response.body());
//                remoteNotes[0] = response.body();
////                Note newNote = new Note("hro_name", "htoText", "MYDIR", false);
//                NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
//                    for (Note remoteNote : remoteNotes[0]) {
//                        remoteNote.setSynced(Boolean.TRUE);
////                        mNoteDao.insert(remoteNote);
//                    }
////                    sync();
//                    return remoteNotes[0];
//                });
//            }
//
//            @Override
//            public void onFailure(Call<List<Note>> call, Throwable t) {
//                //Handle failure
//                System.err.println("READ FAILED");
//                System.out.println(t.toString());
//            }
//        });
    }

    public void initNetworkingStack(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

//        OkHttpClient client = new OkHttpClient.Builder()
//                .build();
//        client.addInterceptor(logging);

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
