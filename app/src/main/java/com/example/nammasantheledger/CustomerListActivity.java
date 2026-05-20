package com.example.nammasantheledger;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity {

    RecyclerView recyclerCustomers;

    EditText etSearch;

    ArrayList<Customer> customerList;

    CustomerAdapter adapter;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_list);

        if (getSupportActionBar() != null) {

            getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }

        db = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "SantheDB"
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        recyclerCustomers =
                findViewById(R.id.recyclerCustomers);

        etSearch =
                findViewById(R.id.etSearch);

        customerList = new ArrayList<>(
                db.customerDao()
                        .getAllCustomers()
        );

        adapter =
                new CustomerAdapter(customerList);

        recyclerCustomers.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerCustomers.setAdapter(adapter);

        etSearch.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {

                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        customerList.clear();

        customerList.addAll(
                db.customerDao()
                        .getAllCustomers()
        );

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();

        return true;
    }
}