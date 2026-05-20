package com.example.nammasantheledger;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String customerName;

    private String productName;

    private int givenAmount;

    private int returnedAmount;

    private int pendingAmount;

    private String date;

    public Transaction(String customerName,
                       String productName,
                       int givenAmount,
                       int returnedAmount,
                       int pendingAmount,
                       String date) {

        this.customerName = customerName;

        this.productName = productName;

        this.givenAmount = givenAmount;

        this.returnedAmount = returnedAmount;

        this.pendingAmount = pendingAmount;

        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getProductName() {
        return productName;
    }

    public int getGivenAmount() {
        return givenAmount;
    }

    public int getReturnedAmount() {
        return returnedAmount;
    }

    public int getPendingAmount() {
        return pendingAmount;
    }

    public String getDate() {
        return date;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setGivenAmount(int givenAmount) {
        this.givenAmount = givenAmount;
    }

    public void setReturnedAmount(int returnedAmount) {
        this.returnedAmount = returnedAmount;
    }

    public void setPendingAmount(int pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public void setDate(String date) {
        this.date = date;
    }
}