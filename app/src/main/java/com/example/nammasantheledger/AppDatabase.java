package com.example.nammasantheledger;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                Customer.class,
                Transaction.class
        },
        version = 2
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CustomerDao customerDao();

    public abstract TransactionDao transactionDao();
}