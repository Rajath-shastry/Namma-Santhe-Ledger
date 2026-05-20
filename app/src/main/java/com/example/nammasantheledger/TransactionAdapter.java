package com.example.nammasantheledger;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    ArrayList<Transaction> transactionList;

    public TransactionAdapter(ArrayList<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item, parent, false);

        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {

        Transaction transaction = transactionList.get(position);

        holder.txtCustomerName.setText(
                transaction.getCustomerName()
        );

        holder.txtProduct.setText(
                "Product : " +
                        transaction.getProductName()
        );

        holder.txtGivenAmount.setText(
                "Given Amount : ₹" +
                        transaction.getGivenAmount()
        );

        holder.txtReturnedAmount.setText(
                "Returned Amount : ₹" +
                        transaction.getReturnedAmount()
        );

        holder.txtPending.setText(
                "Pending Amount : ₹" +
                        transaction.getPendingAmount()
        );

        holder.txtDate.setText(
                "Date : " +
                        transaction.getDate()
        );

        holder.btnEditTransaction.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(
                            holder.itemView.getContext()
                    );

            builder.setTitle("Edit Transaction");

            View dialogView = LayoutInflater.from(
                    holder.itemView.getContext()
            ).inflate(R.layout.edit_transaction_dialog,
                    null);

            EditText etProduct =
                    dialogView.findViewById(R.id.etEditProduct);

            EditText etGiven =
                    dialogView.findViewById(R.id.etEditGiven);

            EditText etReturned =
                    dialogView.findViewById(R.id.etEditReturned);

            EditText etDate =
                    dialogView.findViewById(R.id.etEditDate);

            etProduct.setText(
                    transaction.getProductName()
            );

            etGiven.setText(
                    String.valueOf(
                            transaction.getGivenAmount()
                    )
            );

            etReturned.setText(
                    String.valueOf(
                            transaction.getReturnedAmount()
                    )
            );

            etDate.setText(
                    transaction.getDate()
            );

            builder.setView(dialogView);

            builder.setPositiveButton(
                    "Update",
                    (dialog, which) -> {

                        String updatedProduct =
                                etProduct.getText()
                                        .toString();

                        int updatedGiven =
                                Integer.parseInt(
                                        etGiven.getText()
                                                .toString()
                                );

                        int updatedReturned =
                                Integer.parseInt(
                                        etReturned.getText()
                                                .toString()
                                );

                        int updatedPending =
                                updatedGiven -
                                        updatedReturned;

                        String updatedDate =
                                etDate.getText()
                                        .toString();

                        transaction.setProductName(
                                updatedProduct
                        );

                        transaction.setGivenAmount(
                                updatedGiven
                        );

                        transaction.setReturnedAmount(
                                updatedReturned
                        );

                        transaction.setPendingAmount(
                                updatedPending
                        );

                        transaction.setDate(
                                updatedDate
                        );

                        AppDatabase db = Room.databaseBuilder(
                                        holder.itemView.getContext(),
                                        AppDatabase.class,
                                        "SantheDB"
                                )
                                .allowMainThreadQueries()
                                .fallbackToDestructiveMigration()
                                .build();

                        db.transactionDao()
                                .update(transaction);

                        notifyItemChanged(position);

                        Toast.makeText(
                                holder.itemView.getContext(),
                                "Transaction Updated",
                                Toast.LENGTH_SHORT
                        ).show();
                    });

            builder.setNegativeButton(
                    "Cancel",
                    null
            );

            builder.show();
        });

        holder.btnDeleteTransaction.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(
                            holder.itemView.getContext()
                    );

            builder.setTitle("Delete Transaction");

            builder.setMessage(
                    "Are you sure you want to delete this transaction?"
            );

            builder.setPositiveButton(
                    "Delete",
                    (dialog, which) -> {

                        AppDatabase db = Room.databaseBuilder(
                                        holder.itemView.getContext(),
                                        AppDatabase.class,
                                        "SantheDB"
                                )
                                .allowMainThreadQueries()
                                .fallbackToDestructiveMigration()
                                .build();

                        db.transactionDao().delete(transaction);

                        transactionList.remove(position);

                        notifyItemRemoved(position);

                        notifyItemRangeChanged(
                                position,
                                transactionList.size()
                        );

                        Toast.makeText(
                                holder.itemView.getContext(),
                                "Transaction Deleted",
                                Toast.LENGTH_SHORT
                        ).show();
                    });

            builder.setNegativeButton(
                    "Cancel",
                    null
            );

            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {

        TextView txtCustomerName,
                txtProduct,
                txtGivenAmount,
                txtReturnedAmount,
                txtPending,
                txtDate;

        Button btnEditTransaction,
                btnDeleteTransaction;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCustomerName =
                    itemView.findViewById(R.id.txtCustomerName);

            txtProduct =
                    itemView.findViewById(R.id.txtProduct);

            txtGivenAmount =
                    itemView.findViewById(R.id.txtGivenAmount);

            txtReturnedAmount =
                    itemView.findViewById(R.id.txtReturnedAmount);

            txtPending =
                    itemView.findViewById(R.id.txtPending);

            txtDate =
                    itemView.findViewById(R.id.txtDate);

            btnEditTransaction =
                    itemView.findViewById(R.id.btnEditTransaction);

            btnDeleteTransaction =
                    itemView.findViewById(R.id.btnDeleteTransaction);
        }
    }
}