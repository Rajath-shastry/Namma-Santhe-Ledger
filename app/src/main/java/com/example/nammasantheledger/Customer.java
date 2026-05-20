package com.example.nammasantheledger;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "customers")
public class Customer {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String phone;

    private double totalAmount;
    private double paidAmount;
    private double pendingAmount;

    public Customer(String name, String phone,
                    double totalAmount,
                    double paidAmount,
                    double pendingAmount) {

        this.name = name;
        this.phone = phone;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.pendingAmount = pendingAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public double getPendingAmount() {
        return pendingAmount;
    }
}