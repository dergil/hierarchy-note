package com.github.dergil.hierarchynote.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.github.dergil.hierarchynote.MainActivity;
import com.github.dergil.hierarchynote.R;

public class NoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.github.dergil.hierarchynote.filelistsql.ID";
    public static final String EXTRA_REPLY_NAME = "com.github.dergil.hierarchynote.filelistsql.REPLY_NAME";
    public static final String EXTRA_REPLY_TEXT= "com.github.dergil.hierarchynote.filelistsql.REPLY_TEXT";
    public static final String EXTRA_REQUEST_NAME= "com.github.dergil.hierarchynote.filelistsql.REQUEST_NAME";
    public static final String EXTRA_REQUEST_TEXT= "com.github.dergil.hierarchynote.filelistsql.REQUEST_TEXT";
    public static final String EXTRA_DELETE= "com.github.dergil.hierarchynote.filelistsql.DELETE";

    private EditText mEditNameView;
    private EditText mEditTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        mEditNameView = findViewById(R.id.edit_name);
        mEditTextView = findViewById(R.id.edit_text);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_REQUEST_NAME))
            mEditNameView.setText(intent.getStringExtra(EXTRA_REQUEST_NAME));
        if (intent.hasExtra(EXTRA_REQUEST_TEXT))
            mEditTextView.setText(intent.getStringExtra(EXTRA_REQUEST_TEXT));
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();

        Intent replyIntent = new Intent(getApplicationContext(), MainActivity.class);
        if (TextUtils.isEmpty(mEditNameView.getText()) ) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            String name = mEditNameView.getText().toString();
            String text = mEditTextView.getText().toString();
            replyIntent.putExtra(EXTRA_ID, intent.getLongExtra(EXTRA_ID, -1));
            replyIntent.putExtra(EXTRA_REPLY_NAME, name);
            replyIntent.putExtra(EXTRA_REPLY_TEXT, text);
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = getIntent();

        Intent replyIntent = new Intent(getApplicationContext(), MainActivity.class);
        if (TextUtils.isEmpty(mEditNameView.getText()) ) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            String name = mEditNameView.getText().toString();
            String text = mEditTextView.getText().toString();
            replyIntent.putExtra(EXTRA_ID, intent.getLongExtra(EXTRA_ID, -1));
            replyIntent.putExtra(EXTRA_REPLY_NAME, name);
            replyIntent.putExtra(EXTRA_REPLY_TEXT, text);
            setResult(RESULT_OK, replyIntent);
        }
        finish();
        return true;
    }
}
