package com.example.roomwordsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_NOTE_ACTIVITY_REQUEST_CODE = 2;


    private NoteViewModel mNoteViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final NoteListAdapter adapter = new NoteListAdapter(new NoteListAdapter.WordDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        mNoteViewModel.getAllWords().observe(this, words -> {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(words);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
            Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
            startActivityForResult(intent, NEW_NOTE_ACTIVITY_REQUEST_CODE);
        });

        adapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                intent.putExtra(NewNoteActivity.EXTRA_REQUEST_NAME, note.getName());
                intent.putExtra(NewNoteActivity.EXTRA_REQUEST_TEXT, note.getText());
                startActivityForResult(intent, EDIT_NOTE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data.getStringExtra(NewNoteActivity.EXTRA_DELETE) != null){
//            Note note = new Note(data.getStringExtra(NewNoteActivity.EXTRA_REPLY_NAME), data.getStringExtra(NewNoteActivity.EXTRA_REPLY_TEXT));
            System.out.println("LOGGING HERE LOOK AT ME");
            System.out.println(data.getStringExtra(NewNoteActivity.EXTRA_REPLY_NAME));
            mNoteViewModel.deleteById(data.getStringExtra(NewNoteActivity.EXTRA_REPLY_NAME));
        }

        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Note note = new Note(data.getStringExtra(NewNoteActivity.EXTRA_REPLY_NAME), data.getStringExtra(NewNoteActivity.EXTRA_REPLY_TEXT));
            mNoteViewModel.insert(note);
        }
        if (requestCode == EDIT_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Note note = new Note(data.getStringExtra(NewNoteActivity.EXTRA_REPLY_NAME), data.getStringExtra(NewNoteActivity.EXTRA_REPLY_TEXT));
            mNoteViewModel.update(note);
        }
//        else {
//            Toast.makeText(
//                    getApplicationContext(),
//                    R.string.empty_not_saved,
//                    Toast.LENGTH_LONG).show();
//        }
    }

}