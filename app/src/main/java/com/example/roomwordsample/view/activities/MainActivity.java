package com.example.roomwordsample.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.roomwordsample.view.NoteListAdapter;
import com.example.roomwordsample.R;
import com.example.roomwordsample.model.entity.NoteEntity;
import com.example.roomwordsample.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_NOTE_ACTIVITY_REQUEST_CODE = 2;
    public static final int NEW_DIR_ACTIVITY_REQUEST_CODE = 3;
    public static final int NEW_MAIN_ACTIVITY_REQUEST_CODE = 4;
    public static String DIRECTORY_NAME = "MYDIR";
//    public static String PREVIOUS_DIRECTORY_NAME = "";

//    private NoteListAdapter adapter;
    private NoteViewModel mNoteViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        Intent creationIntent = getIntent();
        if (creationIntent.hasExtra(DIRECTORY_NAME))
            DIRECTORY_NAME = creationIntent.getStringExtra(DIRECTORY_NAME);
//        if (creationIntent.hasExtra(PREVIOUS_DIRECTORY_NAME))
//            PREVIOUS_DIRECTORY_NAME = creationIntent.getStringExtra(PREVIOUS_DIRECTORY_NAME);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        NoteListAdapter adapter = new NoteListAdapter(new NoteListAdapter.WordDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        NoteViewModel.Factory factory = new NoteViewModel.Factory(getApplication());

        mNoteViewModel = new ViewModelProvider(this, factory)
                .get(NoteViewModel.class);

//        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        mNoteViewModel.getAllWords(DIRECTORY_NAME).observe(this, words -> {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(words);
        });

        FloatingActionButton add_note = findViewById(R.id.fab);
        add_note.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            startActivityForResult(intent, NEW_NOTE_ACTIVITY_REQUEST_CODE);
        });

        FloatingActionButton add_dir = findViewById(R.id.add_dir);
        add_dir.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewDirectory.class);
//            intent.putExtra(MainActivity.DIRECTORY_NAME, "MYDIR2");
            startActivityForResult(intent, NEW_DIR_ACTIVITY_REQUEST_CODE);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.
                LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                NoteEntity noteToBeDeleted = adapter.getNoteAt(viewHolder.getAdapterPosition());

                mNoteViewModel.deleteById(noteToBeDeleted.getId());
                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), "File is deleted", Snackbar.LENGTH_LONG)
                        .setDuration(4000)
                        .setAnchorView(add_dir)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                noteToBeDeleted.setSynced(Boolean.FALSE);
                                mNoteViewModel.insert(noteToBeDeleted);
                                Snackbar snackbar1 = Snackbar
                                        .make(findViewById(android.R.id.content), "File is restored!", Snackbar.LENGTH_SHORT)
                                        .setAnchorView(add_dir);
                                snackbar1.show();
                            }
                        });

                snackbar.show();
            }
        }).attachToRecyclerView(recyclerView);




        adapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NoteEntity note) {
                if (note.isDir()) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    String noteName = note.getName();
                    intent.putExtra(MainActivity.DIRECTORY_NAME, noteName);
//                    String dirName = adapter.currentNote.getDirectory_name();
//                    intent.putExtra(MainActivity.PREVIOUS_DIRECTORY_NAME, note.getDirectory_name());
                    startActivityForResult(intent, NEW_MAIN_ACTIVITY_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                    intent.putExtra(NoteActivity.EXTRA_ID, note.getId());
                    intent.putExtra(NoteActivity.EXTRA_REQUEST_NAME, note.getName());
                    intent.putExtra(NoteActivity.EXTRA_REQUEST_TEXT, note.getText());
                    startActivityForResult(intent, EDIT_NOTE_ACTIVITY_REQUEST_CODE);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
//        LiveData<List<Note>> allWords = mNoteViewModel.getAllWords(DIRECTORY_NAME);
//        System.out.println("MAINACTIVITY");
//        System.out.println(allWords.getValue().get(0));
//        try {
//            Note parentDirNote = mNoteViewModel.getParentDir(DIRECTORY_NAME);
//            String parentDir = parentDirNote.getDirectory_name();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        String parentDir = adapter.currentNote.getDirectory_name();
        intent.putExtra(MainActivity.DIRECTORY_NAME, "MYDIR");
//        intent.putExtra(MainActivity.DIRECTORY_NAME, PREVIOUS_DIRECTORY_NAME);
        startActivityForResult(intent, NEW_MAIN_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String name = data.getStringExtra(NoteActivity.EXTRA_REPLY_NAME);
            String text = data.getStringExtra(NoteActivity.EXTRA_REPLY_TEXT);
            NoteEntity note = new NoteEntity(name, text, DIRECTORY_NAME, false, false);
            mNoteViewModel.insert(note);
        }
        if (requestCode == EDIT_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
//            Note note = mNoteViewModel.findByName(data.getStringExtra(NewNoteActivity.EXTRA_REPLY_NAME));
//            note.setText(data.getStringExtra(NewNoteActivity.EXTRA_REPLY_TEXT));
            long id = data.getLongExtra(NoteActivity.EXTRA_ID, -1);
            String name = data.getStringExtra(NoteActivity.EXTRA_REPLY_NAME);
            String text = data.getStringExtra(NoteActivity.EXTRA_REPLY_TEXT);
            NoteEntity note = new NoteEntity(name, text, DIRECTORY_NAME, false, false);
            note.setId(id);
            mNoteViewModel.update(note);
        }
        if (requestCode == NEW_DIR_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            NoteEntity note = new NoteEntity(data.getStringExtra(NewDirectory.EXTRA_REPLY_DIRECTORY_NAME), "filler",
                    DIRECTORY_NAME, true, false);
            mNoteViewModel.insert(note);
        }
        if (data.getStringExtra(NoteActivity.EXTRA_DELETE) != null && resultCode == RESULT_OK) {
//            Note note = new Note(data.getStringExtra(NewNoteActivity.EXTRA_REPLY_NAME), data.getStringExtra(NewNoteActivity.EXTRA_REPLY_TEXT));
//            System.out.println("LOGGING HERE LOOK AT ME");
//            System.out.println(data.getStringExtra(NewNoteActivity.EXTRA_REPLY_NAME));
            long id = data.getLongExtra(NoteActivity.EXTRA_ID, -1);
            mNoteViewModel.deleteById(id);
        }

//        else {
//            Toast.makeText(
//                    getApplicationContext(),
//                    R.string.empty_not_saved,
//                    Toast.LENGTH_LONG).show();
//        }
    }
}