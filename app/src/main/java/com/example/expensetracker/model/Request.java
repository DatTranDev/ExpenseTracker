package com.example.expensetracker.model;

public class Request {
    private String senderId;
    private String receiverId;
    private String walletId;
    private String name;
    private AppUser sender;
    private AppUser receiver;
    private Wallet wallet;

    public Request() {
    }

    public Request(String senderId, String receiverId, String walletId, String name, AppUser sender, AppUser receiver, Wallet wallet) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.walletId = walletId;
        this.name = name;
        this.sender = sender;
        this.receiver = receiver;
        this.wallet = wallet;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AppUser getSender() {
        return sender;
    }

    public void setSender(AppUser sender) {
        this.sender = sender;
    }

    public AppUser getReceiver() {
        return receiver;
    }

    public void setReceiver(AppUser receiver) {
        this.receiver = receiver;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
