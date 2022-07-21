package com.svalero.toteco_app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.toteco_app.domain.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    List<User> findByUsernameAndPassword(String username, String password);

    @Insert
    void insert(User user);

    @Query("UPDATE user SET password = :password WHERE id = :id")
    void updatePassword(int id, String password);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
