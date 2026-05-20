package com.example.nammasantheledger;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class AddTransactionActivity extends AppCompatActivity {

    EditText etCustomerName,
            etProductName,
            etGivenAmount,
            etReturnedAmount,
            etDate;

    Button btnSaveTransaction;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_transaction);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "SantheDB"
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        etCustomerName =
                findViewById(R.id.etCustomerName);

        etProductName =
                findViewById(R.id.etProductName);

        etGivenAmount =
                findViewById(R.id.etGivenAmount);

        etReturnedAmount =
                findViewById(R.id.etReturnedAmount);

        etDate =
                findViewById(R.id.etDate);

        btnSaveTransaction =
                findViewById(R.id.btnSaveTransaction);

        btnSaveTransaction.setOnClickListener(v -> {

            String customerName =
                    etCustomerName.getText().toString().trim();

            String productName =
                    etProductName.getText().toString().trim();

            String givenAmount =
                    etGivenAmount.getText().toString().trim();

            String returnedAmount =
                    etReturnedAmount.getText().toString().trim();

            String date =
                    etDate.getText().toString().trim();

            if (customerName.isEmpty()
                    || productName.isEmpty()
                    || givenAmount.isEmpty()
                    || returnedAmount.isEmpty()
                    || date.isEmpty()) {

                Toast.makeText(
                        this,
                        "Enter all details",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                int given =
                        Integer.parseInt(givenAmount);

                int returned =
                        Integer.parseInt(returnedAmount);

                int pending =
                        given - returned;

                Customer existingCustomer =
                        db.customerDao()
                                .getCustomerByPhone(customerName);

                if (existingCustomer == null) {

                    Customer newCustomer =
                            new Customer(
                                    customerName,
                                    customerName,
                                    0,
                                    0,
                                    0
                            );

                    db.customerDao().insert(newCustomer);
                }

                Transaction transaction =
                        new Transaction(
                                customerName,
                                productName,
                                given,
                                returned,
                                pending,
                                date
                        );

                db.transactionDao().insert(transaction);

                Toast.makeText(
                        this,
                        "Transaction Saved Successfully",
                        Toast.LENGTH_LONG
                ).show();

                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();

        return true;
    }
}