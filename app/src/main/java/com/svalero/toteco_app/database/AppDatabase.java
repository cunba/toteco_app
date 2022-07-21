package com.svalero.toteco_app.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.svalero.toteco_app.dao.UserDao;
import com.svalero.toteco_app.domain.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
