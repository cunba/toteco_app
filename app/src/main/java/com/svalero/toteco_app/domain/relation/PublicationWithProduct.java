package com.svalero.toteco_app.domain.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.svalero.toteco_app.domain.Product;
import com.svalero.toteco_app.domain.Publication;

import java.util.List;

public class PublicationWithProduct {
    @Embedded
    private Publication publication;

    @Relation(parentColumn = "id", entityColumn = "publication_id")
    private List<Product> products;

    public PublicationWithProduct(Publication publication, List<Product> products) {
        this.publication = publication;
        this.products = products;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
