package com.example.roomwordsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

public class NewNoteActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_NAME = "com.example.android.wordlistsql.REPLY_NAME";
    public static final String EXTRA_REPLY_TEXT= "com.example.android.wordlistsql.REPLY_TEXT";
    public static final String EXTRA_REQUEST_NAME= "com.example.android.wordlistsql.REQUEST_NAME";
    public static final String EXTRA_REQUEST_TEXT= "com.example.android.wordlistsql.REQUEST_TEXT";
//    public static final String EXTRA_DELETE= "com.example.android.wordlistsql.DELETE";

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

        final Button button_save = findViewById(R.id.button_save);
        button_save.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditNameView.getText()) || TextUtils.isEmpty(mEditNameView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String name = mEditNameView.getText().toString();
                String text = mEditTextView.getText().toString();
                replyIntent.putExtra(EXTRA_REPLY_NAME, name);
                replyIntent.putExtra(EXTRA_REPLY_TEXT, text);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });

//        final Button button_delete = findViewById(R.id.button_delete);
//        button_delete.setOnClickListener(view -> {
//            Intent replyIntent = new Intent();
//            replyIntent.putExtra(EXTRA_DELETE, "DELETE");
//            setResult(RESULT_OK, replyIntent);
//        });
    }
}
