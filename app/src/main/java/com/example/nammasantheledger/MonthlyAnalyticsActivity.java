package com.example.nammasantheledger;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class MonthlyAnalyticsActivity
        extends AppCompatActivity {

    TextView txtMonthlySales,
            txtMonthlyReturned,
            txtMonthlyPending;

    PieChart monthlyPieChart;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_monthly_analytics
        );

        if (getSupportActionBar() != null) {

            getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }

        txtMonthlySales =
                findViewById(R.id.txtMonthlySales);

        txtMonthlyReturned =
                findViewById(R.id.txtMonthlyReturned);

        txtMonthlyPending =
                findViewById(R.id.txtMonthlyPending);

        monthlyPieChart =
                findViewById(R.id.monthlyPieChart);

        db = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "SantheDB"
                )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        int totalPending =
                db.transactionDao()
                        .getTotalPending();

        int totalReturned =
                db.transactionDao()
                        .getTotalReturned();

        int totalSales =
                totalPending + totalReturned;

        txtMonthlySales.setText(
                "₹" + totalSales
        );

        txtMonthlyReturned.setText(
                "₹" + totalReturned
        );

        txtMonthlyPending.setText(
                "₹" + totalPending
        );

        ArrayList<PieEntry> entries =
                new ArrayList<>();

        entries.add(
                new PieEntry(
                        totalSales,
                        "Sales"
                )
        );

        entries.add(
                new PieEntry(
                        totalReturned,
                        "Returned"
                )
        );

        entries.add(
                new PieEntry(
                        totalPending,
                        "Pending"
                )
        );

        PieDataSet dataSet =
                new PieDataSet(
                        entries,
                        "Monthly Analytics"
                );

        dataSet.setColors(
                new int[]{

                        android.graphics.Color.parseColor("#C8E6C9"),

                        android.graphics.Color.parseColor("#BBDEFB"),

                        android.graphics.Color.parseColor("#FFE0B2")

                }
        );

        PieData data =
                new PieData(dataSet);

        data.setValueTextSize(16f);

        data.setValueTextColor(
                android.graphics.Color.BLACK
        );

        monthlyPieChart.setData(data);

        monthlyPieChart.getDescription()
                .setEnabled(false);

        monthlyPieChart.setHoleRadius(58f);

        monthlyPieChart.setTransparentCircleRadius(62f);

        monthlyPieChart.setEntryLabelColor(
                android.graphics.Color.BLACK
        );

        monthlyPieChart.animateY(1000);

        monthlyPieChart.invalidate();
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();

        return true;
    }
}