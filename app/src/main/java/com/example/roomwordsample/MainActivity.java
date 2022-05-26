package com.example.roomwordsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_NOTE_ACTIVITY_REQUEST_CODE = 2;
    public static final int NEW_DIR_ACTIVITY_REQUEST_CODE = 3;
    public static final int NEW_MAIN_ACTIVITY_REQUEST_CODE = 4;
    public static String DIRECTORY_NAME = "MYDIR";
    public static String PREVIOUS_DIRECTORY_NAME;


    private NoteViewModel mNoteViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        Intent creationIntent = getIntent();
        if (creationIntent.hasExtra(DIRECTORY_NAME))
            DIRECTORY_NAME = creationIntent.getStringExtra(DIRECTORY_NAME);
        if (creationIntent.hasExtra(PREVIOUS_DIRECTORY_NAME))
            PREVIOUS_DIRECTORY_NAME = creationIntent.getStringExtra(PREVIOUS_DIRECTORY_NAME);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final NoteListAdapter adapter = new NoteListAdapter(new NoteListAdapter.WordDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        mNoteViewModel.getAllWords(DIRECTORY_NAME).observe(this, words -> {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(words);
        });

        FloatingActionButton add_note = findViewById(R.id.fab);
        add_note.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
            startActivityForResult(intent, NEW_NOTE_ACTIVITY_REQUEST_CODE);
        });

        FloatingActionButton add_dir = findViewById(R.id.add_dir);
        add_dir.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewDirectory.class);
//            intent.putExtra(MainActivity.DIRECTORY_NAME, "MYDIR2");
            startActivityForResult(intent, NEW_DIR_ACTIVITY_REQUEST_CODE);
        });

        adapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                if (note.isDir()) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.DIRECTORY_NAME, note.getName());
                    intent.putExtra(MainActivity.PREVIOUS_DIRECTORY_NAME, DIRECTORY_NAME);
                    startActivityForResult(intent, NEW_MAIN_ACTIVITY_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                    intent.putExtra(NewNoteActivity.EXTRA_ID, note.getId());
                    intent.putExtra(NewNoteActivity.EXTRA_REQUEST_NAME, note.getName());
                    intent.putExtra(NewNoteActivity.EXTRA_REQUEST_TEXT, note.getText());
                    startActivityForResult(intent, EDIT_NOTE_ACTIVITY_REQUEST_CODE);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.DIRECTORY_NAME, PREVIOUS_DIRECTORY_NAME);
        intent.putExtra(MainActivity.PREVIOUS_DIRECTORY_NAME, DIRECTORY_NAME);
        startActivityForResult(intent, NEW_MAIN_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String name = data.getStringExtra(NewNoteActivity.EXTRA_REPLY_NAME);
            String text = data.getStringExtra(NewNoteActivity.EXTRA_REPLY_TEXT);
            Note note = new Note(name, text, DIRECTORY_NAME, false);
            mNoteViewModel.insert(note);
        }
        if (requestCode == EDIT_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
//            Note note = mNoteViewModel.findByName(data.getStringExtra(NewNoteActivity.EXTRA_REPLY_NAME));
//            note.setText(data.getStringExtra(NewNoteActivity.EXTRA_REPLY_TEXT));
            long id = data.getLongExtra(NewNoteActivity.EXTRA_ID, -1);
            String name = data.getStringExtra(NewNoteActivity.EXTRA_REPLY_NAME);
            String text = data.getStringExtra(NewNoteActivity.EXTRA_REPLY_TEXT);
            Note note = new Note(name, text, DIRECTORY_NAME, false);
            note.setId(id);
            mNoteViewModel.update(note);
        }
        if (requestCode == NEW_DIR_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Note note = new Note(data.getStringExtra(NewDirectory.EXTRA_REPLY_DIRECTORY_NAME), "filler",
                    DIRECTORY_NAME, true);
            mNoteViewModel.insert(note);
        }
        if (data.getStringExtra(NewNoteActivity.EXTRA_DELETE) != null && resultCode == RESULT_OK) {
//            Note note = new Note(data.getStringExtra(NewNoteActivity.EXTRA_REPLY_NAME), data.getStringExtra(NewNoteActivity.EXTRA_REPLY_TEXT));
//            System.out.println("LOGGING HERE LOOK AT ME");
//            System.out.println(data.getStringExtra(NewNoteActivity.EXTRA_REPLY_NAME));
            long id = data.getLongExtra(NewNoteActivity.EXTRA_ID, -1);
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