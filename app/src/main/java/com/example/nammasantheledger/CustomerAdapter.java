package com.example.nammasantheledger;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> implements Filterable {

    ArrayList<Customer> customerList;

    ArrayList<Customer> customerListFull;

    public CustomerAdapter(ArrayList<Customer> customerList) {

        this.customerList = customerList;

        this.customerListFull = new ArrayList<>();

        this.customerListFull.addAll(customerList);
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_item,
                        parent,
                        false);

        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder,
                                 int position) {

        Customer customer = customerList.get(position);

        holder.txtName.setText(
                customer.getName()
        );

        holder.txtPhone.setText(
                customer.getPhone()
        );

        AppDatabase db = Room.databaseBuilder(
                        holder.itemView.getContext(),
                        AppDatabase.class,
                        "SantheDB"
                )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        List<Transaction> transactionList =
                db.transactionDao().getAllTransactions();

        int totalPending = 0;

        for (Transaction transaction : transactionList) {

            if (transaction.getCustomerName()
                    .equalsIgnoreCase(customer.getName())) {

                totalPending =
                        totalPending +
                                transaction.getPendingAmount();
            }
        }

        holder.txtPending.setText(
                "Pending : ₹" + totalPending
        );

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    holder.itemView.getContext(),
                    CustomerLedgerActivity.class
            );

            intent.putExtra(
                    "customerName",
                    customer.getName()
            );

            intent.putExtra(
                    "customerPhone",
                    customer.getPhone()
            );

            holder.itemView.getContext()
                    .startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {

        return customerList.size();
    }

    public static class CustomerViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtName,
                txtPhone,
                txtPending;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName =
                    itemView.findViewById(R.id.txtName);

            txtPhone =
                    itemView.findViewById(R.id.txtPhone);

            txtPending =
                    itemView.findViewById(R.id.txtPending);
        }
    }

    @Override
    public Filter getFilter() {

        return customerFilter;
    }

    private final Filter customerFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(
                CharSequence constraint) {

            ArrayList<Customer> filteredList =
                    new ArrayList<>();

            if (constraint == null
                    || constraint.length() == 0) {

                filteredList.addAll(customerListFull);

            } else {

                String filterPattern =
                        constraint.toString()
                                .toLowerCase()
                                .trim();

                for (Customer item : customerListFull) {

                    if (item.getName()
                            .toLowerCase()
                            .contains(filterPattern)) {

                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();

            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(
                CharSequence constraint,
                FilterResults results) {

            customerList.clear();

            customerList.addAll(
                    (ArrayList<Customer>) results.values
            );

            notifyDataSetChanged();
        }
    };
}