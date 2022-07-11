package com.example.roomwordsample.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "note_table", indices = {@Index(value = "name", unique = true)})
public class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private Long id;

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

    @ColumnInfo(name = "synced")
    @SerializedName("synced")
    @Expose
    private transient Boolean synced = false;

    public NoteEntity(String name, String text, String directory_name, Boolean isDir, Boolean synced) {
        this.name = name;
        this.text = text;
        this.directory_name = directory_name;
        this.isDir = isDir;
        this.synced = synced;
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

    public Boolean isDir() {
        return isDir;
    }

    public void setDir(Boolean dir) {
        isDir = dir;
    }

    public Boolean getSynced() {
        return synced;
    }

    public void setSynced(Boolean synced) {
        this.synced = synced;
    }
}


