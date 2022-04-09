package com.example.roomwordsample;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "text")
    private String text;

    public Note(@NonNull String mName, String text) {
        this.mName = mName;
        this.text = text;
    }


//    public Note(@NonNull String mName) {
//        this.mName = mName;
//    }

    @NonNull
    public String getName() {
        return mName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}


