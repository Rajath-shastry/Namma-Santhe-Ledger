package com.example.nammasantheledger;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CustomerLedgerActivity extends AppCompatActivity {

    TextView txtCustomerName,
            txtCustomerPhone,
            txtTotalPending;

    Button btnWhatsappReminder,
            btnGeneratePdf;

    RecyclerView recyclerCustomerTransactions;

    AppDatabase db;

    ArrayList<Transaction> customerTransactionList;

    TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_ledger);

        if (getSupportActionBar() != null) {

            getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }

        txtCustomerName =
                findViewById(R.id.txtCustomerName);

        txtCustomerPhone =
                findViewById(R.id.txtCustomerPhone);

        txtTotalPending =
                findViewById(R.id.txtTotalPending);

        btnWhatsappReminder =
                findViewById(R.id.btnWhatsappReminder);

        btnGeneratePdf =
                findViewById(R.id.btnGeneratePdf);

        recyclerCustomerTransactions =
                findViewById(
                        R.id.recyclerCustomerTransactions
                );

        String customerName =
                getIntent().getStringExtra(
                        "customerName"
                );

        String customerPhone =
                getIntent().getStringExtra(
                        "customerPhone"
                );

        txtCustomerName.setText(customerName);

        txtCustomerPhone.setText(
                "Phone : " + customerPhone
        );

        db = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "SantheDB"
                )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        List<Transaction> allTransactions =
                db.transactionDao()
                        .getAllTransactions();

        customerTransactionList =
                new ArrayList<>();

        int totalPending = 0;

        for (Transaction transaction :
                allTransactions) {

            if (transaction.getCustomerName()
                    .equalsIgnoreCase(customerName)) {

                customerTransactionList.add(transaction);

                totalPending =
                        totalPending +
                                transaction.getPendingAmount();
            }
        }

        int finalPending = totalPending;

        txtTotalPending.setText(
                "Total Pending : ₹" + totalPending
        );

        adapter =
                new TransactionAdapter(
                        customerTransactionList
                );

        recyclerCustomerTransactions
                .setLayoutManager(
                        new LinearLayoutManager(this)
                );

        recyclerCustomerTransactions
                .setAdapter(adapter);

        btnWhatsappReminder.setOnClickListener(v -> {

            try {

                String message =
                        "Hello " + customerName +
                                ",\n\nYour pending amount is ₹" +
                                finalPending +
                                ".\nPlease clear the payment.\n\n- Sri Lakshmi Vegetables";

                String url =
                        "https://wa.me/91" +
                                customerPhone +
                                "?text=" +
                                URLEncoder.encode(
                                        message,
                                        "UTF-8"
                                );

                Intent intent =
                        new Intent(
                                Intent.ACTION_VIEW
                        );

                intent.setData(Uri.parse(url));

                startActivity(intent);

            } catch (Exception e) {

                Toast.makeText(
                        this,
                        "WhatsApp not installed",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        btnGeneratePdf.setOnClickListener(v -> {

            PdfDocument pdfDocument =
                    new PdfDocument();

            PdfDocument.PageInfo pageInfo =
                    new PdfDocument.PageInfo.Builder(
                            600,
                            900,
                            1
                    ).create();

            PdfDocument.Page page =
                    pdfDocument.startPage(pageInfo);

            Paint titlePaint = new Paint();

            titlePaint.setTextSize(28);

            titlePaint.setTypeface(
                    Typeface.create(
                            Typeface.DEFAULT,
                            Typeface.BOLD
                    )
            );

            Paint textPaint = new Paint();

            textPaint.setTextSize(18);

            Paint linePaint = new Paint();

            linePaint.setStrokeWidth(2);

            int y = 60;

            page.getCanvas().drawText(
                    "Namma Santhe Ledger",
                    150,
                    y,
                    titlePaint
            );

            y += 50;

            page.getCanvas().drawLine(
                    20,
                    y,
                    580,
                    y,
                    linePaint
            );

            y += 40;

            page.getCanvas().drawText(
                    "Customer : " + customerName,
                    30,
                    y,
                    textPaint
            );

            y += 30;

            page.getCanvas().drawText(
                    "Phone : " + customerPhone,
                    30,
                    y,
                    textPaint
            );

            y += 50;

            for (Transaction transaction :
                    customerTransactionList) {

                page.getCanvas().drawText(
                        "Product : "
                                + transaction.getProductName(),
                        30,
                        y,
                        textPaint
                );

                y += 25;

                page.getCanvas().drawText(
                        "Given Amount : ₹"
                                + transaction.getGivenAmount(),
                        50,
                        y,
                        textPaint
                );

                y += 25;

                page.getCanvas().drawText(
                        "Returned Amount : ₹"
                                + transaction.getReturnedAmount(),
                        50,
                        y,
                        textPaint
                );

                y += 25;

                page.getCanvas().drawText(
                        "Pending Amount : ₹"
                                + transaction.getPendingAmount(),
                        50,
                        y,
                        textPaint
                );

                y += 25;

                page.getCanvas().drawText(
                        "Date : "
                                + transaction.getDate(),
                        50,
                        y,
                        textPaint
                );

                y += 35;

                page.getCanvas().drawLine(
                        30,
                        y,
                        560,
                        y,
                        linePaint
                );

                y += 35;
            }

            page.getCanvas().drawText(
                    "TOTAL PENDING : ₹" + finalPending,
                    30,
                    y,
                    titlePaint
            );

            y += 60;

            page.getCanvas().drawText(
                    "Thank You Visit Again",
                    170,
                    y,
                    textPaint
            );

            pdfDocument.finishPage(page);

            File downloadsFolder =
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS
                    );

            File file =
                    new File(
                            downloadsFolder,
                            customerName + "_Ledger_Report.pdf"
                    );

            try {

                pdfDocument.writeTo(
                        new FileOutputStream(file)
                );

                Toast.makeText(
                        this,
                        "PDF Saved in Downloads",
                        Toast.LENGTH_LONG
                ).show();

            } catch (Exception e) {

                Toast.makeText(
                        this,
                        "PDF Generation Failed",
                        Toast.LENGTH_SHORT
                ).show();
            }

            pdfDocument.close();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();

        return true;
    }
}