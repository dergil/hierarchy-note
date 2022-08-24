package com.github.dergil.hierarchynote.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.github.dergil.hierarchynote.model.entity.JwtEntity;

import java.util.List;

@Dao
public interface JwtDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(JwtEntity jwt);

    @Query("SELECT * FROM jwt_table")
    List<JwtEntity> findAll();

    @Query("DELETE FROM jwt_table")
    void deleteAll();
}
