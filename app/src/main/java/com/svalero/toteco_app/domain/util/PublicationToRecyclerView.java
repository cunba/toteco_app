package com.svalero.toteco_app.domain.util;

import android.graphics.Bitmap;

import com.svalero.toteco_app.domain.Product;

import java.util.List;

public class PublicationToRecyclerView {
    private int publicationId;
    private String establishmentName;
    private String establishmentPunctuation;
    private byte[] image;
    private List<Product> products;
    private String totalPrice;
    private String totalPunctuation;

    public PublicationToRecyclerView(int publicationId, String establishmentName, String establishmentPunctuation, byte[] image, List<Product> products, String totalPrice, String totalPunctuation) {
        this.publicationId = publicationId;
        this.establishmentName = establishmentName;
        this.establishmentPunctuation = establishmentPunctuation;
        this.image = image;
        this.products = products;
        this.totalPrice = totalPrice;
        this.totalPunctuation = totalPunctuation;
    }

    public int getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(int publicationId) {
        this.publicationId = publicationId;
    }

    public String getEstablishmentName() {
        return establishmentName;
    }

    public void setEstablishmentName(String establishmentName) {
        this.establishmentName = establishmentName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalPunctuation() {
        return totalPunctuation;
    }

    public void setTotalPunctuation(String totalPunctuation) {
        this.totalPunctuation = totalPunctuation;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getEstablishmentPunctuation() {
        return establishmentPunctuation;
    }

    public void setEstablishmentPunctuation(String establishmentPunctuation) {
        this.establishmentPunctuation = establishmentPunctuation;
    }
}
