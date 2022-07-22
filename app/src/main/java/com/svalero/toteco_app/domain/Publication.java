package com.svalero.toteco_app.domain;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "publications",foreignKeys = {
    @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id", onDelete = ForeignKey.CASCADE),
    @ForeignKey(entity = Establishment.class, parentColumns = "id", childColumns = "establishment_id", onDelete = ForeignKey.CASCADE)
})
public class Publication {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "total_price")
    private float totalPrice;
    @ColumnInfo(name = "total_punctuation")
    private float totalPunctuation;
    @ColumnInfo
    private byte[] image;
    @ColumnInfo(name = "user_id")
    private int userId;
    @ColumnInfo(name = "establishment_id")
    private int establishmentId;

    public Publication(float totalPrice, float totalPunctuation, byte[] image, int userId, int establishmentId) {
        this.totalPrice = totalPrice;
        this.totalPunctuation = totalPunctuation;
        this.image = image;
        this.userId = userId;
        this.establishmentId = establishmentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(int establishmentId) {
        this.establishmentId = establishmentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getTotalPunctuation() {
        return totalPunctuation;
    }

    public void setTotalPunctuation(float totalPunctuation) {
        this.totalPunctuation = totalPunctuation;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
