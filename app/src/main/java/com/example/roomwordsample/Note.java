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

    @ColumnInfo(name = "directory_name")
    private String directory_name;

    @ColumnInfo(name = "isDir")
    private boolean isDir;

    public Note(@NonNull String mName, String text, String directory_name, boolean isDir) {
        this.mName = mName;
        this.text = text;
        this.directory_name = directory_name;
        this.isDir = isDir;
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

    public String getDirectory_name() {
        return directory_name;
    }

    public boolean isDir() {
        return isDir;
    }
}


