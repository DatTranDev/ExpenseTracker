package com.example.expensetracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Wallet implements Parcelable {
    private String id;
    private String name;
    private BigDecimal amount;
    private String currency;
    private boolean isSharing;
    private List<AppUser> members;

    // Constructor
    public Wallet() {
    }
    public Wallet(String id, String name, BigDecimal amount, String currency, boolean isSharing) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.currency = currency;
        this.isSharing = isSharing;
        this.members = new ArrayList<>();
    }

    // Getters and setters
    public List<AppUser> getMembers() {
        return members;
    }
    public void setMembers(List<AppUser> members) {
        this.members = members;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getCurrency() {
        return currency;
    }
    public boolean isSharing() {
        return isSharing;
    }

    public void setSharing(boolean sharing) {
        isSharing = sharing;
    }


    protected Wallet(Parcel in) {
        id = in.readString();
        name = in.readString();
        amount = new BigDecimal(in.readString());
        currency = in.readString();
        isSharing = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(amount.toPlainString());
        dest.writeString(currency);
        dest.writeByte((byte) (isSharing ? 1 : 0));
    }

    public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel in) {
            return new Wallet(in);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };

    public void addMember(AppUser user) {
        if (members != null && !members.contains(user)) {
            members.add(user);
        }
    }
}

