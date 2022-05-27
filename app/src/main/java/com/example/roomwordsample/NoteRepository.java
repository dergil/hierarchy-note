package com.example.roomwordsample;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


class NoteRepository {

    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllWords;
    public Note parentDir;
    public static final String DIRECTORY_NAME = "MYDIR";

    static final String BASE_URL = "http://10.0.2.2:8080/";
    ServerDB serverDB;


    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        start();
        mNoteDao = db.wordDao();
        mAllWords = mNoteDao.getAlphabetizedWords(DIRECTORY_NAME);
    }

    private void start() {
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

//        Call<List<File>> call = serverDB.loadChanges("status:open");
//        call.enqueue(this);

        List<Note> remoteNotes = new ArrayList<>();
        Call<List<Note>> questions1 = serverDB.getNotes();
        questions1.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                System.out.println(response.body());
                System.out.println(response.isSuccessful());
//                remoteNotes.addAll(response.body());
                List<Note> remoteNotes = response.body();
                Note newNote = new Note("hro_name", "htoText", "MYDIR", false);
                NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
                    for (Note remoteNote : remoteNotes) {
                        mNoteDao.insert(remoteNote);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                //Handle failure
                System.err.println("READ FAILED");
                System.out.println(t.toString());
            }
        });
    }

//    Note getParentDir(String directory_name) {
//        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
//            parentDir = mNoteDao.getParentDir(directory_name);
//        });
//        return parentDir;
//    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Note>> getAllWords(String directory_name) {
        mAllWords = mNoteDao.getAlphabetizedWords(directory_name);
//        tempSet.addAll(mAllWords.getValue());
//        TODO: remote und local diffen, dann mergen durch hinzufügen zu lokaler DB
//        tempSet.addAll(remoteNotes);
        return mAllWords;

//        OkHttpClient client = new OkHttpClient();
//        String url = "http://10.0.2.2:8080/api/core/file/find-all";
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            System.out.println(response.body().string());
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }

    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Note note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            Long id = mNoteDao.insert(note);
            note.setId(id);


//            MediaType JSON = MediaType.get("application/json; charset=utf-8");
//            String url = "http://10.0.2.2:8080/api/core/file/create";
//            String json = "{\"id\": 47, \"name\": \"gil2\", \"text\": \"text\", \"directory_name\": \"root\", \"isDir\": false }";
//
//            OkHttpClient client = new OkHttpClient();
//
//                RequestBody body = RequestBody.create(json, JSON);
//                Request request = new Request.Builder()
//                        .url(url)
//                        .post(body)
//                        .build();
//                try (Response response = client.newCall(request).execute()) {
//                    System.out.println(response.body().string());
//                } catch (IOException exception) {
//                    exception.printStackTrace();
//                }



            Call<ResponseDto> questions = serverDB.saveNote(note);
            try {
                Response<ResponseDto> execute = questions.execute();
                ResponseDto body = execute.body();
                System.out.println(body.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    void update(Note note){
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            Long id = note.getId();
            Note oldNote = mNoteDao.find(id);

            if (!oldNote.getName().equals(note.getName())) {
                sendUpdate(id, new UpdateFileDto("replace", "/name", note.getName()));
            }
            if (!oldNote.getText().equals(note.getText())) {
                sendUpdate(id, new UpdateFileDto("replace", "/text", note.getText()));
            }

            mNoteDao.update(note);

        });
    }

    void sendUpdate(Long id, UpdateFileDto updateFileDto){
        List<UpdateFileDto> updateBody = new ArrayList<>();
        updateBody.add(updateFileDto);
        Call<ResponseDto> questions = serverDB.editNote(id, updateBody);
        try {
            Response<ResponseDto> execute = questions.execute();
            ResponseDto body = execute.body();
            System.out.println(body.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void delete(Long id){
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.deleteById(id);

            Call<ResponseDto> questions = serverDB.deleteNote(id);
            try {
                Response<ResponseDto> execute = questions.execute();
                System.out.println(execute.isSuccessful());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

