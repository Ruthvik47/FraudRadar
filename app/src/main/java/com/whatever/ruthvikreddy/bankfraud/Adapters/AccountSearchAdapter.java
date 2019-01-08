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

import com.whatever.ruthvikreddy.bankfraud.Activities.DisplayBankDetails;
import com.whatever.ruthvikreddy.bankfraud.Model.Bankdetails;
import com.whatever.ruthvikreddy.bankfraud.R;

import java.util.List;
import java.util.zip.Inflater;

public class AccountSearchAdapter extends RecyclerView.Adapter<AccountSearchAdapter.MyViewHolder> {
    private Context context;
    private List<Bankdetails> bankdetails;

    public AccountSearchAdapter() {
    }

    public AccountSearchAdapter(Context context, List<Bankdetails> bankdetails) {
        this.context = context;
        this.bankdetails = bankdetails;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.bankfrauddetails,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Bankdetails details = bankdetails.get(i);
        myViewHolder.fraudname.setText(details.getFraudname());
        myViewHolder.accountname.setText(details.getAccountnumber());
        myViewHolder.bankname.setText(details.getBankname());

        myViewHolder.bankLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(context,DisplayBankDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("accountnumber",details.getAccountnumber());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return bankdetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView fraudname;
        private TextView bankname;
        private TextView accountname;
        private CardView bankLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fraudname = (TextView)itemView.findViewById(R.id.fraudname);
            bankname = (TextView)itemView.findViewById(R.id.bankname);
            accountname = (TextView)itemView.findViewById(R.id.accountnumber);
            bankLayout= (CardView)itemView.findViewById(R.id.bankLayout);

        }
    }
}
