package com.example.nammasantheledger;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {

    RecyclerView recyclerTransactions;

    ArrayList<Transaction> transactionList;

    TransactionAdapter adapter;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transaction);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerTransactions =
                findViewById(R.id.recyclerTransactions);

        db = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "SantheDB"
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        List<Transaction> savedTransactions =
                db.transactionDao().getAllTransactions();

        transactionList = new ArrayList<>(savedTransactions);

        adapter = new TransactionAdapter(transactionList);

        recyclerTransactions.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerTransactions.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();

        return true;
    }
}