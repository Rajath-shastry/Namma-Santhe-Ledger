package com.example.nammasantheledger;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class AddCustomerActivity extends AppCompatActivity {

    EditText etCustomerName, etPhone;

    Button btnSaveCustomer;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_customer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etCustomerName =
                findViewById(R.id.etCustomerName);

        etPhone =
                findViewById(R.id.etPhone);

        btnSaveCustomer =
                findViewById(R.id.btnSaveCustomer);

        db = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "SantheDB"
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        btnSaveCustomer.setOnClickListener(v -> {

            String name =
                    etCustomerName.getText()
                            .toString()
                            .trim();

            String phone =
                    etPhone.getText()
                            .toString()
                            .trim();

            if (name.isEmpty() || phone.isEmpty()) {

                Toast.makeText(
                        this,
                        "Enter all details",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Customer existingCustomer =
                        db.customerDao()
                                .getCustomerByPhone(phone);

                if (existingCustomer != null) {

                    Toast.makeText(
                            this,
                            "Customer already exists",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent = new Intent(
                            AddCustomerActivity.this,
                            CustomerLedgerActivity.class
                    );

                    intent.putExtra(
                            "customerName",
                            existingCustomer.getName()
                    );

                    intent.putExtra(
                            "customerPhone",
                            existingCustomer.getPhone()
                    );

                    startActivity(intent);

                } else {

                    Customer customer = new Customer(
                            name,
                            phone,
                            0,
                            0,
                            0
                    );

                    db.customerDao().insert(customer);

                    Toast.makeText(
                            this,
                            "Customer Saved",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent = new Intent(
                            AddCustomerActivity.this,
                            CustomerListActivity.class
                    );

                    startActivity(intent);

                    finish();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();

        return true;
    }
}