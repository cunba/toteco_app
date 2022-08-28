package com.svalero.toteco_app.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "products", foreignKeys = {
        @ForeignKey(entity = Publication.class, parentColumns = "id", childColumns = "publication_id", onDelete = ForeignKey.CASCADE)
    },
    indices = {@Index(value = "publication_id", unique = false)}
)
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private float price;
    @ColumnInfo
    private float punctuation;
    @ColumnInfo(name = "publication_id")
    private int publicationId;

    public Product(String name, float price, float punctuation, int publicationId) {
        this.price = price;
        this.punctuation = punctuation;
        this.name = name;
        this.publicationId = publicationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPunctuation() {
        return punctuation;
    }

    public void setPunctuation(float punctuation) {
        this.punctuation = punctuation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(int publicationId) {
        this.publicationId = publicationId;
    }

    @NonNull
    @Override
    public String toString() {
        return "- " + name + "\n   (" + price + "€, " + punctuation + "/5)";
    }
}
