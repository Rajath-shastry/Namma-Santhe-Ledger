package com.example.nammasantheledger;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CustomerDao {

    @Insert
    void insert(Customer customer);

    @Query("SELECT * FROM customers")
    List<Customer> getAllCustomers();

    @Query("SELECT COUNT(*) FROM customers")
    int getCustomerCount();

    @Query("SELECT * FROM customers WHERE phone = :phone LIMIT 1")
    Customer getCustomerByPhone(String phone);
}