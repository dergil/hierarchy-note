package com.github.dergil.hierarchynote.model.repository;

import androidx.lifecycle.LiveData;

import com.github.dergil.hierarchynote.model.network.NoteAPI;
import com.github.dergil.hierarchynote.model.db.NoteRoomDatabase;
import com.github.dergil.hierarchynote.model.network.ServerDB;
import com.github.dergil.hierarchynote.model.dao.NoteDao;
import com.github.dergil.hierarchynote.model.dto.ResponseDto;
import com.github.dergil.hierarchynote.model.dto.UpdateFileDto;
import com.github.dergil.hierarchynote.model.entity.NoteEntity;

import java.util.List;


public class NoteRepository implements NoteRepositoryInterface{

    private static NoteRepository sInstance;
    private NoteRoomDatabase mDatabase;
    private NoteDao mNoteDao;
    private LiveData<List<NoteEntity>> mAllWords;
    NoteAPI networking;
    public static String DIRECTORY_NAME = "MYDIR";

    static String BASE_URL = "http://10.0.2.2:8080/";
    ServerDB serverDB;

    public NoteRepository(NoteRoomDatabase database, NoteAPI networking) {
        mDatabase = database;
        mNoteDao = database.wordDao();
        mAllWords = mNoteDao.getAlphabetizedWords(DIRECTORY_NAME);
        this.networking = networking;
        start();
//        mObservableProducts = new MediatorLiveData<>();

//        mObservableProducts.addSource(mDatabase.productDao().loadAllProducts(),
//                productEntities -> {
//                    if (mDatabase.getDatabaseCreated().getValue() != null) {
//                        mObservableProducts.postValue(productEntities);
//                    }
//                });
    }


    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
//    NoteRepository(Application application, Networking networking) {
//        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
//        this.networking = networking;
////        networking = new Networking();
//        start();
//        mNoteDao = db.wordDao();
//        mAllWords = mNoteDao.getAlphabetizedWords(DIRECTORY_NAME);
//    }

    public static NoteRepository getInstance(final NoteRoomDatabase database, NoteAPI networking) {
        if (sInstance == null) {
            synchronized (NoteRepository.class) {
                if (sInstance == null) {
                    sInstance = new NoteRepository(database, networking);
                }
            }
        }
        return sInstance;
    }

    private void start() {
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//
////        OkHttpClient client = new OkHttpClient.Builder()
////                .build();
////        client.addInterceptor(logging);
//
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(logging);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(httpClient.build())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        serverDB = retrofit.create(ServerDB.class);




        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<NoteEntity> remoteNotes = networking.getNotes();
            List<NoteEntity> localFiles = mNoteDao.findAll();
            if (remoteNotes != null) {
                for (NoteEntity remoteNote : remoteNotes) {
                    if (!localFiles.contains(remoteNote)){
                        remoteNote.setSynced(Boolean.TRUE);
                        mNoteDao.insert(remoteNote);
                    }
                }
            }
            sync();
        });




//        Call<List<Note>> questions1 = serverDB.getNotes();
//        questions1.enqueue(new Callback<List<Note>>() {
//            @Override
//            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
//                System.out.println(response.body());
//                System.out.println(response.isSuccessful());
////                remoteNotes.addAll(response.body());
//                List<Note> remoteNotes = response.body();
////                Note newNote = new Note("hro_name", "htoText", "MYDIR", false);
//                NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
//                    for (Note remoteNote : remoteNotes) {
//                        remoteNote.setSynced(Boolean.TRUE);
//                        mNoteDao.insert(remoteNote);
//                    }
//                    sync();
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

//    Note getParentDir(String directory_name) {
//        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
//            parentDir = mNoteDao.getParentDir(directory_name);
//        });
//        return parentDir;
//    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<NoteEntity>> getAllWords(String directory_name) {
        mAllWords = mNoteDao.getAlphabetizedWords(directory_name);
        return mAllWords;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(NoteEntity note) {
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {



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

            ResponseDto response = networking.insert(note);
            if (response != null){
                note.setId(response.getId());
                note.setSynced(Boolean.TRUE);
            }
            mNoteDao.insert(note);


//            Call<ResponseDto> questions = serverDB.saveNote(note);
//            try {
//                Response<ResponseDto> execute = questions.execute();
//                ResponseDto body = execute.body();
//                if (execute.isSuccessful()){
//                    note.setId(body.getId());
//                    note.setSynced(Boolean.TRUE);
//                }
//                mNoteDao.insert(note);
//            } catch (IOException e) {
//                e.printStackTrace();
//                mNoteDao.insert(note);
//            }
        });
    }

    private void sync() {
        List<NoteEntity> localFiles = mNoteDao.findAll();
        Long id = null;
        for (NoteEntity localFile : localFiles) {
            if (!localFile.getSynced()) {
                id = insertRemotely(localFile);
            }
            if (id != null){
//                Long oldFileId = localFile.getId();
//                mNoteDao.deleteById(oldFileId);
                localFile.setId(id);
                localFile.setSynced(Boolean.TRUE);
//                mNoteDao.insert(localFile);
                mNoteDao.update(localFile);
            }
        }
    }
    private Long insertRemotely(NoteEntity note) {
        ResponseDto response = networking.insert(note);
        if (response != null)
            return response.getId();
        else return null;
    }

    public void update(NoteEntity note){
//        TODO: update requests idealerweise zusammenfassen, wegen synced status
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            Long id = note.getId();
            NoteEntity oldNote = mNoteDao.find(id);
//            boolean successfulRemoteNameChange = false;
            if (!oldNote.getName().equals(note.getName())) {
                networking.update(id, new UpdateFileDto("replace", "/name", note.getName()));
            }
//            if (!successfulRemoteNameChange)
//                note.setSynced(Boolean.FALSE);

//            boolean successfulRemoteTextChange = false;
            if (!oldNote.getText().equals(note.getText())) {
                networking.update(id, new UpdateFileDto("replace", "/text", note.getText()));
            }
//            if (!successfulRemoteNameChange)
//                note.setSynced(Boolean.FALSE);
            mNoteDao.update(note);
        });
    }

//    boolean sendUpdate(Long id, UpdateFileDto updateFileDto){
//        List<UpdateFileDto> updateBody = new ArrayList<>();
//        updateBody.add(updateFileDto);
//        Call<ResponseDto> questions = serverDB.editNote(id, updateBody);
//        try {
//            Response<ResponseDto> execute = questions.execute();
//            if (execute.isSuccessful())
//                return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    public void delete(Long id){
        NoteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mNoteDao.deleteById(id);
            networking.delete(id);
//            try {
//                Call<ResponseDto> questions = serverDB.deleteNote(id);
//                Response<ResponseDto> execute = questions.execute();
//                System.out.println(execute.isSuccessful());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        });
    }
}

