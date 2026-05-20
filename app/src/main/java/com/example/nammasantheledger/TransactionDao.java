package com.example.nammasantheledger;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionDao {

    @Insert
    void insert(Transaction transaction);

    @Update
    void update(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Query("SELECT * FROM transactions")
    List<Transaction> getAllTransactions();

    @Query("SELECT SUM(pendingAmount) FROM transactions")
    int getTotalPending();

    @Query("SELECT SUM(returnedAmount) FROM transactions")
    int getTotalReturned();
}