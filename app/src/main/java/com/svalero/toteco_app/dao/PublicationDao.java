package com.svalero.toteco_app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.svalero.toteco_app.domain.Publication;
import com.svalero.toteco_app.domain.relation.EstablishmentWithPublication;
import com.svalero.toteco_app.domain.relation.PublicationWithProduct;
import com.svalero.toteco_app.domain.relation.UserWithPublication;

import java.util.List;

@Dao
public interface PublicationDao {

    @Query("SELECT * FROM publications WHERE id = :id")
    Publication findById(int id);

    @Query("SELECT * FROM publications")
    List<Publication> findAll();

    @Query("SELECT * FROM publications WHERE id != 1")
    List<Publication> findAllExceptAux();

    @Query("SELECT * FROM publications ORDER BY id DESC LIMIT 1")
    Publication findLast();

    @Query("SELECT id FROM publications ORDER BY id DESC LIMIT 1")
    int findLastId();

    @Insert
    void insert(Publication publication);

    @Update
    void update(Publication publication);

    @Delete
    void delete(Publication publication);

    @Transaction
    @Query("SELECT * FROM establishments WHERE id = :establishmentId")
    EstablishmentWithPublication findEstablishmentById(int establishmentId);

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    UserWithPublication findUserById(int userId);

    @Query("SELECT SUM(price) AS suma FROM products WHERE publication_id = :id")
    float totalPrice(int id);

    @Query("SELECT SUM(suma)/SUM(num) FROM (SELECT SUM(punctuation) AS suma, COUNT(id) AS num FROM products WHERE publication_id = :id)")
    float totalPunctuation(int id);
}
