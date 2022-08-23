package com.github.dergil.hierarchynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.dergil.hierarchynote.model.entity.FileEntity;
import com.github.dergil.hierarchynote.view.activities.NewDirectoryActivity;
import com.github.dergil.hierarchynote.view.activities.NoteActivity;
import com.github.dergil.hierarchynote.view.activities.ui.login.LoginActivity;
import com.github.dergil.hierarchynote.view.recycler_view.FileListAdapter;
import com.github.dergil.hierarchynote.viewmodel.FileViewModel;
import com.github.dergil.hierarchynote.viewmodel.FileViewModelInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_NOTE_ACTIVITY_REQUEST_CODE = 2;
    public static final int NEW_DIR_ACTIVITY_REQUEST_CODE = 3;
    public static final int NEW_MAIN_ACTIVITY_REQUEST_CODE = 4;
    public static String DIRECTORY_NAME = "MYDIR";
    private FileViewModelInterface mFileViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        Intent creationIntent = getIntent();
        if (creationIntent.hasExtra(DIRECTORY_NAME))
            DIRECTORY_NAME = creationIntent.getStringExtra(DIRECTORY_NAME);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        FileListAdapter adapter = new FileListAdapter(new FileListAdapter.WordDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FileViewModel.Factory factory = new FileViewModel.Factory(getApplication());

        mFileViewModel = new ViewModelProvider(this, factory)
                .get(FileViewModel.class);

        mFileViewModel.getAllFiles(DIRECTORY_NAME).observe(this, words -> {
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
            Intent intent = new Intent(MainActivity.this, NewDirectoryActivity.class);
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
                FileEntity fileToBeDeleted = adapter.getFileAt(viewHolder.getAdapterPosition());

                mFileViewModel.deleteById(fileToBeDeleted.getId());
                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), "File is deleted", Snackbar.LENGTH_LONG)
                        .setDuration(4000)
//                        .setAnchorView(add_dir)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fileToBeDeleted.setSynced(Boolean.FALSE);
                                mFileViewModel.insert(fileToBeDeleted);
                                Snackbar snackbar1 = Snackbar
                                        .make(findViewById(android.R.id.content), "File is restored!", Snackbar.LENGTH_SHORT);
//                                        .setAnchorView(add_dir);
                                snackbar1.show();
                            }
                        });

                snackbar.show();
            }
        }).attachToRecyclerView(recyclerView);




        adapter.setOnItemClickListener(new FileListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FileEntity file) {
                if (file.isDir()) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    String fileName = file.getName();
                    intent.putExtra(MainActivity.DIRECTORY_NAME, fileName);
                    startActivityForResult(intent, NEW_MAIN_ACTIVITY_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                    intent.putExtra(NoteActivity.EXTRA_ID, file.getId());
                    intent.putExtra(NoteActivity.EXTRA_REQUEST_NAME, file.getName());
                    intent.putExtra(NoteActivity.EXTRA_REQUEST_TEXT, file.getText());
                    startActivityForResult(intent, EDIT_NOTE_ACTIVITY_REQUEST_CODE);
                }
            }
        });

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, EDIT_NOTE_ACTIVITY_REQUEST_CODE);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.DIRECTORY_NAME, "MYDIR");
        startActivityForResult(intent, NEW_MAIN_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String name = data.getStringExtra(NoteActivity.EXTRA_REPLY_NAME);
            String text = data.getStringExtra(NoteActivity.EXTRA_REPLY_TEXT);
            FileEntity file = new FileEntity(name, text, DIRECTORY_NAME, false, false);
            mFileViewModel.insert(file);
        }
        if (requestCode == EDIT_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            long id = data.getLongExtra(NoteActivity.EXTRA_ID, -1);
            String name = data.getStringExtra(NoteActivity.EXTRA_REPLY_NAME);
            String text = data.getStringExtra(NoteActivity.EXTRA_REPLY_TEXT);
            FileEntity file = new FileEntity(name, text, DIRECTORY_NAME, false, false);
            file.setId(id);
            mFileViewModel.update(file);
        }
        if (requestCode == NEW_DIR_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            FileEntity file = new FileEntity(data.getStringExtra(NewDirectoryActivity.EXTRA_REPLY_DIRECTORY_NAME), "filler",
                    DIRECTORY_NAME, true, false);
            mFileViewModel.insert(file);
        }
        if (data.getStringExtra(NoteActivity.EXTRA_DELETE) != null && resultCode == RESULT_OK) {
            long id = data.getLongExtra(NoteActivity.EXTRA_ID, -1);
            mFileViewModel.deleteById(id);
        }
    }
}