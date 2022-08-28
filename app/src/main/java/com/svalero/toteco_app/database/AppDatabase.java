package com.svalero.toteco_app.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.svalero.toteco_app.dao.EstablishmentDao;
import com.svalero.toteco_app.dao.ProductDao;
import com.svalero.toteco_app.dao.PublicationDao;
import com.svalero.toteco_app.dao.UserDao;
import com.svalero.toteco_app.domain.Establishment;
import com.svalero.toteco_app.domain.Product;
import com.svalero.toteco_app.domain.Publication;
import com.svalero.toteco_app.domain.User;

@Database(entities = {Establishment.class, Product.class, Publication.class, User.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EstablishmentDao establishmentDao();

    public abstract ProductDao productDao();

    public abstract PublicationDao publicationDao();

    public abstract UserDao userDao();
}
