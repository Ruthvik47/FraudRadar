package com.whatever.ruthvikreddy.bankfraud.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whatever.ruthvikreddy.bankfraud.Activities.DisplayPhonedetails;
import com.whatever.ruthvikreddy.bankfraud.Model.PhoneDetails;
import com.whatever.ruthvikreddy.bankfraud.R;

import java.util.List;

public class PhoneSearchAdapter extends RecyclerView.Adapter<PhoneSearchAdapter.MyViewHolder> {
    private Context context;
    private List<PhoneDetails> phoneDetails;

    public PhoneSearchAdapter() {
    }

    public PhoneSearchAdapter(Context context, List<PhoneDetails> phoneDetails) {
        this.context = context;
        this.phoneDetails = phoneDetails;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.phonefrauddetails,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final PhoneDetails details = phoneDetails.get(i);
        myViewHolder.fraudname.setText(details.getFraudname());
        myViewHolder.walletname.setText(details.getWalletname());
        myViewHolder.phonenumber.setText(details.getPhonenumber());

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DisplayPhonedetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("fraudname",details.getFraudname());
                intent.putExtra("walletname",details.getWalletname());
                intent.putExtra("transaction",details.getTransactionid());
                intent.putExtra("phonenumber",details.getPhonenumber());
                intent.putExtra("complaint",details.getDescription());
                intent.putExtra("postedby",details.getAnonymous());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return phoneDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView fraudname;
        private TextView walletname;
        private TextView phonenumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fraudname = (TextView)itemView.findViewById(R.id.fraudname);
            walletname = (TextView)itemView.findViewById(R.id.walletname);
            phonenumber = (TextView)itemView.findViewById(R.id.phonenumber);
            cardView = (CardView)itemView.findViewById(R.id.cardview);

        }
    }
}
