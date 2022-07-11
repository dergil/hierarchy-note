package com.github.dergil.hierarchynote.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.github.dergil.hierarchynote.R;


public class NewDirectory extends AppCompatActivity {
    public static final String EXTRA_REPLY_DIRECTORY_NAME =
            "com.example.android.wordlistsql.REPLY_DIRECTORY_NAME";

    private EditText mEditNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_directory);
        mEditNameView = findViewById(R.id.edit_name);

        final Button button_save = findViewById(R.id.button_save);
        button_save.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditNameView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String name = mEditNameView.getText().toString();
                replyIntent.putExtra(EXTRA_REPLY_DIRECTORY_NAME, name);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(mEditNameView.getText())) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            String name = mEditNameView.getText().toString();
            replyIntent.putExtra(EXTRA_REPLY_DIRECTORY_NAME, name);
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }
}