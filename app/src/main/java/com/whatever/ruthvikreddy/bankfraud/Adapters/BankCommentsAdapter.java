package com.whatever.ruthvikreddy.bankfraud.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whatever.ruthvikreddy.bankfraud.Model.Bankcomments;
import com.whatever.ruthvikreddy.bankfraud.R;

import java.util.List;

public class BankCommentsAdapter extends RecyclerView.Adapter<BankCommentsAdapter.MyViewHolder> {
    private Context context;
    private List<Bankcomments > bankcommentsList;

    public BankCommentsAdapter() {
    }

    public BankCommentsAdapter(Context context, List<Bankcomments> bankcommentsList) {
        this.context = context;
        this.bankcommentsList = bankcommentsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.commentslayout,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Bankcomments bankcomments = bankcommentsList.get(i);
        myViewHolder.postedby.setText(bankcomments.getPostedby());
        myViewHolder.comment.setText(bankcomments.getComment());

    }

    @Override
    public int getItemCount() {
        return bankcommentsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView comment;
        private TextView postedby;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = (TextView)itemView.findViewById(R.id.comment);
            postedby = (TextView)itemView.findViewById(R.id.posteduser);
        }
    }
}
