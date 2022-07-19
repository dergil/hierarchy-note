package com.github.dergil.hierarchynote.local_tests.unit.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.dergil.hierarchynote.model.dto.UpdateFileDto;
import com.github.dergil.hierarchynote.model.entity.FileEntity;
import com.github.dergil.hierarchynote.model.dto.ResponseDto;
import com.github.dergil.hierarchynote.model.network.ServerDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(AndroidJUnit4.class)
public class NetworkUnitTests {
//    static final String BASE_URL = "http://10.0.2.2:8080/";
//    static final String BASE_URL = "http://127.0.0.1:8080/";
    ServerDB serverDB;

    MockWebServer mockWebServer;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        HttpUrl BASE_URL = mockWebServer.url("");
//        System.out.println(baseUrl);
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
    public void createsAndSendsInsertRequest() throws IOException, InterruptedException {
        String body = "{id=5, name='name', text='text', directory='MYDIR', isDir=false, synced=false}";
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(body));

        FileEntity note = new FileEntity("name", "text", "MYDIR", false, false);
        Call<ResponseDto> questions = serverDB.saveFile(note);
        questions.execute();
        RecordedRequest request1 = mockWebServer.takeRequest();
        System.out.println(request1.toString());
        assertEquals("POST", request1.getMethod());
        assertEquals("/api/core/file/create", request1.getPath());
        containsFileAsJson(note, request1.getBody().readUtf8());
    }

    @Test
    public void createsAndSendsUpdateRequest() throws IOException, InterruptedException {
        Long id = 3L;
        String body = "{id=5, name='name', text='text', directory='MYDIR', isDir=false, synced=false}";
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(body));

        UpdateFileDto updateFileDto = new UpdateFileDto("replace", "/text", "hro");
        List<UpdateFileDto> updateBody = new ArrayList<>();
        updateBody.add(updateFileDto);
        Call<ResponseDto> questions = serverDB.editFile(id, updateBody);
        questions.execute();
        RecordedRequest request1 = mockWebServer.takeRequest();
        assertEquals("PUT", request1.getMethod());
        assertEquals("/api/core/file/update?id=" + id, request1.getPath());
        containsUpdateDtoAsJson(updateFileDto, request1.getBody().readUtf8());
    }

    @Test
    public void createsAndSendsDeleteRequest() throws IOException, InterruptedException {
        Long id = 3L;
        String body = "{id=5, name='name', text='text', directory='MYDIR', isDir=false, synced=false}";
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(body));

        Call<ResponseDto> questions = serverDB.deleteFile(id);
        questions.execute();
        RecordedRequest request1 = mockWebServer.takeRequest();
        assertEquals("DELETE", request1.getMethod());
        assertEquals("/api/core/file/delete?id=" + id, request1.getPath());

    }

    @Test
    public void createsAndSendsReadAllRequest() throws IOException, InterruptedException {
        String note = "[{id=5, name='name', text='text', directory='MYDIR', isDir=false, synced=false}]";
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(note));

        Call<List<FileEntity>> questions = serverDB.getFiles();
        questions.execute();
        RecordedRequest request1 = mockWebServer.takeRequest();
        assertEquals("GET", request1.getMethod());
        assertEquals("/api/core/file/find-all", request1.getPath());

    }



    @Test
    public void editNote() {

    }

    private void containsFileAsJson(FileEntity file, String requestBody){
        assertTrue(requestBody.contains("\"name\":\"" + file.getName() + "\""));
        assertTrue(requestBody.contains("\"text\":\"" + file.getText() + "\""));
        assertTrue(requestBody.contains("\"directory_name\":\"" + file.getDirectory_name() + "\""));
        assertTrue(requestBody.contains("\"isDir\":" + file.isDir()));
    }

    private void containsUpdateDtoAsJson(UpdateFileDto updateFileDto, String requestBody){
        System.out.println(requestBody);
        assertTrue(requestBody.contains("\"op\":\"" + updateFileDto.getOp() + "\""));
        assertTrue(requestBody.contains("\"path\":\"" + updateFileDto.getPath() + "\""));
        assertTrue(requestBody.contains("\"value\":\"" + updateFileDto.getValue() + "\""));

    }
}
