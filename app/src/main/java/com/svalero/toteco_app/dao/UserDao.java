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

    @Query("SELECT * FROM users")
    List<User> findAll();

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    List<User> findByUsernameAndPassword(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username")
    List<User> findByUsername(String username);

    @Query("SELECT id FROM users WHERE username = :username")
    List<Integer> findIdByUsername(String username);

    @Insert
    void insert(User user);

    @Query("UPDATE users SET password = :password WHERE id = :id")
    void updatePassword(int id, String password);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
