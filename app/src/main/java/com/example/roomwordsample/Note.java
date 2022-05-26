package com.example.roomwordsample;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private Long id;

//    @PrimaryKey
//    @NonNull
    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;

    @ColumnInfo(name = "text")
    @SerializedName("text")
    @Expose
    private String text;

    @ColumnInfo(name = "directory_name")
    @SerializedName("directory_name")
    @Expose
    private String directory_name;

    @ColumnInfo(name = "isDir")
    @SerializedName("isDir")
    @Expose
    private Boolean isDir;

    public Note(String name, String text, String directory_name, boolean isDir) {
        this.name = name;
        this.text = text;
        this.directory_name = directory_name;
        this.isDir = isDir;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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


