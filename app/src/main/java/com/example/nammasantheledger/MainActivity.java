package com.example.nammasantheledger;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAddCustomer,
            btnAddTransaction,
            btnTransactionHistory,
            btnMonthlyAnalytics;

    PieChart pieChart;

    TextView txtTotalCustomers,
            txtTotalPending,
            txtPending;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnAddCustomer =
                findViewById(R.id.btnAddCustomer);

        btnAddTransaction =
                findViewById(R.id.btnAddTransaction);

        btnTransactionHistory =
                findViewById(R.id.btnTransactionHistory);

        btnMonthlyAnalytics =
                findViewById(R.id.btnMonthlyAnalytics);

        pieChart =
                findViewById(R.id.pieChart);

        txtTotalCustomers =
                findViewById(R.id.txtTotalCustomers);

        txtTotalPending =
                findViewById(R.id.txtTotalPending);

        txtPending =
                findViewById(R.id.txtPending);

        db = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "SantheDB"
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        loadDashboardData();

        btnAddCustomer.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddCustomerActivity.class
            );

            startActivity(intent);
        });

        btnAddTransaction.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddTransactionActivity.class
            );

            startActivity(intent);
        });

        btnTransactionHistory.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    TransactionActivity.class
            );

            startActivity(intent);
        });

        btnMonthlyAnalytics.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    MonthlyAnalyticsActivity.class
            );

            startActivity(intent);
        });
    }

    private void loadDashboardData() {

        int customerCount =
                db.customerDao().getCustomerCount();

        int totalPending =
                db.transactionDao().getTotalPending();

        int totalReturned =
                db.transactionDao().getTotalReturned();

        txtTotalCustomers.setText(
                String.valueOf(customerCount)
        );

        txtTotalPending.setText(
                "₹" + totalPending
        );

        txtPending.setText(
                "₹ " + totalPending
        );

        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(
                totalPending,
                "Pending"
        ));

        entries.add(new PieEntry(
                totalReturned,
                "Paid"
        ));

        PieDataSet dataSet =
                new PieDataSet(
                        entries,
                        "Ledger Report"
                );

        dataSet.setColors(
                new int[]{

                        android.graphics.Color.parseColor("#C8E6C9"),

                        android.graphics.Color.parseColor("#BBDEFB")

                }
        );

        PieData data =
                new PieData(dataSet);

        data.setValueTextSize(16f);

        data.setValueTextColor(
                android.graphics.Color.BLACK
        );

        pieChart.setData(data);

        pieChart.getDescription()
                .setEnabled(false);

        pieChart.setHoleRadius(58f);

        pieChart.setTransparentCircleRadius(62f);

        pieChart.setHoleColor(
                android.graphics.Color.parseColor("#F5F5F5")
        );

        pieChart.setEntryLabelColor(
                android.graphics.Color.BLACK
        );

        pieChart.animateY(1000);

        pieChart.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadDashboardData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.main_menu,
                menu
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_search) {

            showSearchDialog();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSearchDialog() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Search Customer");

        final EditText input = new EditText(this);

        input.setHint("Enter Customer Name");

        builder.setView(input);

        builder.setPositiveButton(
                "Search",
                (dialog, which) -> {

                    String searchName =
                            input.getText()
                                    .toString()
                                    .trim();

                    Customer foundCustomer = null;

                    for (Customer customer :
                            db.customerDao().getAllCustomers()) {

                        if (customer.getName()
                                .equalsIgnoreCase(searchName)) {

                            foundCustomer = customer;

                            break;
                        }
                    }

                    if (foundCustomer != null) {

                        AlertDialog.Builder resultDialog =
                                new AlertDialog.Builder(this);

                        resultDialog.setTitle(
                                "Customer Details"
                        );

                        resultDialog.setMessage(
                                "Name : "
                                        + foundCustomer.getName()
                                        + "\n\nPhone : "
                                        + foundCustomer.getPhone()
                                        + "\n\nPending Amount : ₹"
                                        + foundCustomer.getPendingAmount()
                        );

                        resultDialog.setPositiveButton(
                                "OK",
                                null
                        );

                        resultDialog.show();

                    } else {

                        AlertDialog.Builder notFoundDialog =
                                new AlertDialog.Builder(this);

                        notFoundDialog.setTitle(
                                "Not Found"
                        );

                        notFoundDialog.setMessage(
                                "Customer not found"
                        );

                        notFoundDialog.setPositiveButton(
                                "OK",
                                null
                        );

                        notFoundDialog.show();
                    }
                });

        builder.setNegativeButton(
                "Cancel",
                null
        );

        builder.show();
    }
}