package com.example.expensetracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class TransactionExp implements Parcelable {
    private String id;
    private String userId;
    private String categoryId;
    private String note;
    private BigDecimal spend;
    private String currency;
    private String partner;
    private String walletId;
    private Timestamp createdAt;
    private String image;

    // Constructor
    public TransactionExp(String id, String userId, String categoryId, String note, BigDecimal spend, String currency, String partner, String walletId, Timestamp createdAt, String image ) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.note = note;
        this.spend = spend;
        this.currency = currency;
        this.partner = partner;
        this.walletId = walletId;
        this.createdAt = createdAt;
        this.image = image;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getSpend() {
        return spend;
    }

    public void setSpend(BigDecimal spend) {
        this.spend = spend;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    protected TransactionExp(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        categoryId = in.readInt();
        note = in.readString();
        spend = (BigDecimal) in.readSerializable();
        currencyId = in.readInt();
        partner = in.readString();
        walletId = in.readInt();
        createdAt = (Timestamp) in.readSerializable();
        image = in.readString();
    }

    public static final Creator<TransactionExp> CREATOR = new Creator<TransactionExp>() {
        @Override
        public TransactionExp createFromParcel(Parcel in) {
            return new TransactionExp(in);
        }

        @Override
        public TransactionExp[] newArray(int size) {
            return new TransactionExp[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeInt(categoryId);
        dest.writeString(note);
        dest.writeSerializable(spend);
        dest.writeInt(currencyId);
        dest.writeString(partner);
        dest.writeInt(walletId);
        dest.writeSerializable(createdAt);
        dest.writeString(image);
    }
    public String getImage() {
        return partner;
    }

    public void setImage(String partner) {
        this.partner = partner;
    }
}
