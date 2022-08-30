package com.svalero.toteco_app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.svalero.toteco_app.domain.Product;
import com.svalero.toteco_app.domain.relation.PublicationWithProduct;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products WHERE publication_id = :publicationId")
    List<Product> findByPublicationId(int publicationId);

    @Query("SELECT * FROM products WHERE id = :id")
    Product findById(int id);

    @Query("SELECT * FROM products")
    List<Product> findAll();

    @Transaction
    @Query("SELECT * FROM publications WHERE id = (SELECT publication_id FROM products WHERE id = :id)")
    PublicationWithProduct findPublicationByProductId(int id);

    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("DELETE FROM products WHERE publication_id = :publication_id")
    void deleteByPublicationId(int publication_id);
}
