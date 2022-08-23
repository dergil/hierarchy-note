package com.github.dergil.hierarchynote.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "jwt_table", indices = {@Index(value = "jwt", unique = true)})
public class JwtEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @ColumnInfo(name = "jwt")
    @SerializedName("jwt")
    @Expose
    private String jwt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
