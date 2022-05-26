package com.example.roomwordsample;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private long id;

//    @PrimaryKey
//    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "directory_name")
    private String directory_name;

    @ColumnInfo(name = "isDir")
    private boolean isDir;

    public Note(String name, String text, String directory_name, boolean isDir) {
        this.name = name;
        this.text = text;
        this.directory_name = directory_name;
        this.isDir = isDir;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setDirectory_name(String directory_name) {
        this.directory_name = directory_name;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }
}


