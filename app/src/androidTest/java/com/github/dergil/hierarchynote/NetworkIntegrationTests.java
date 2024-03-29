package com.github.dergil.hierarchynote;

import static org.junit.Assert.assertTrue;

import com.github.dergil.hierarchynote.model.dto.ResponseDto;
import com.github.dergil.hierarchynote.model.dto.UpdateFileDto;
import com.github.dergil.hierarchynote.model.entity.FileEntity;
import com.github.dergil.hierarchynote.model.network.FileServerDB;
import com.github.dergil.hierarchynote.local_tests.util.TestUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// backend needs to be running
public class NetworkIntegrationTests {

    FileServerDB serverDB;
    static final String BASE_URL = "http://10.0.2.2:8080/";
//    static final String BASE_URL = "http://127.0.0.1:8080/";
    TestUtil testUtil = new TestUtil();
    FileEntity note = new FileEntity("name", "text", "MYDIR", false, false);


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

        serverDB = retrofit.create(FileServerDB.class);
    }



    @Test
    public void insertFile() throws IOException {
        FileEntity note = new FileEntity("name", "text", "MYDIR", false, false);
        Call<ResponseDto> questions = serverDB.saveFile(note);
        Response<ResponseDto> response = questions.execute();
        System.out.println(response.body());
        assertTrue(response.isSuccessful());
        assertTrue(testUtil.compareNotesWithoutId(response.body(), note));
    }

    @Test
    public void updateFile() throws IOException, InterruptedException {
        Long id = insertNoteHelper();
        String newText = "hro";
        UpdateFileDto updateFileDto = new UpdateFileDto("replace", "/text", newText);
        List<UpdateFileDto> updateBody = new ArrayList<>();
        updateBody.add(updateFileDto);
        Call<ResponseDto> questions = serverDB.editFile(id, updateBody);
        Response<ResponseDto> response = questions.execute();

        note.setText(newText);
        assertTrue(response.isSuccessful());
        assertTrue(testUtil.compareNotesWithoutId(response.body(), note));
    }

    @Test
    public void deleteFile() throws IOException, InterruptedException {
        Long id = insertNoteHelper();
        Call<ResponseDto> questions = serverDB.deleteFile(id);
        Response<ResponseDto> response = questions.execute();
        assertTrue(response.isSuccessful());
    }

    @Test
    public void readFiles() throws IOException, InterruptedException {
        Long id = insertNoteHelper();
        Call<List<FileEntity>> questions = serverDB.getFiles();
        Response<List<FileEntity>> response = questions.execute();
        List<FileEntity> body = response.body();
        FileEntity file = findById(body, id);
        assertTrue(response.isSuccessful());
        assertTrue(testUtil.compareNotesWithoutId(file, note));
    }

    public FileEntity findById(Collection<FileEntity> list, Long id) {
        return list.stream().filter(carnet -> id.equals(carnet.getId())).findFirst().orElse(null);
        }

    private Long insertNoteHelper() throws IOException, InterruptedException {
        Call<ResponseDto> questions = serverDB.saveFile(note);
        Response<ResponseDto> response = questions.execute();
        Thread.sleep(100);

//        find key id with a number as value
        Pattern p = Pattern.compile("id=\\d+");
        Matcher m = p.matcher(response.body().toString());
        m.find();
        return Long.parseLong(m.group(0).split("=")[1]);
    }
}
