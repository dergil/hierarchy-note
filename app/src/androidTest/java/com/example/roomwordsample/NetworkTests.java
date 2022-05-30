package com.example.roomwordsample;

import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//TODO: ist das nötig? Bzw sinnvoll, da wir vom Rechner aus testen, nicht vom handy aus (siehe IP)
public class NetworkTests {
    static final String BASE_URL = "http://127.0.0.1:8080/";
    ServerDB serverDB;

    TestUtil testUtil = new TestUtil();

    @Before
    public void setUp() throws Exception {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        serverDB = retrofit.create(ServerDB.class);
    }

    @Test
    public void insertNoteSuccess() {
        Note note = testUtil.createNote1();

        Call<ResponseDto> questions = serverDB.saveNote(note);
        try {
            Response<ResponseDto> response = questions.execute();
            assertTrue(response.isSuccessful());
            assertTrue(testUtil.compareNotes(response.body(), note));
            }
        catch (IOException exception) {
            exception.printStackTrace();
        }


    }
}
