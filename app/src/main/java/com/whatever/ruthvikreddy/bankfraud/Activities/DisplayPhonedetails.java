package com.whatever.ruthvikreddy.bankfraud.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whatever.ruthvikreddy.bankfraud.R;

public class DisplayPhonedetails extends AppCompatActivity {
    private String transactionid,walletname,fraudname,phonenumber,complaint;
    private TextView name,id,wallet,number,username,comment;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phonedetails);

        fraudname = getIntent().getStringExtra("fraudname");
        walletname = getIntent().getStringExtra("walletname");
        transactionid = getIntent().getStringExtra("transaction");
        phonenumber = getIntent().getStringExtra("phonenumber");
        complaint = getIntent().getStringExtra("complaint");
        fraudname = getIntent().getStringExtra("fraudname");

        name = (TextView)findViewById(R.id.fraudname);
        id = (TextView)findViewById(R.id.transactionid);
        wallet = (TextView)findViewById(R.id.walletname);
        number = (TextView)findViewById(R.id.phonenumber);
        username = (TextView)findViewById(R.id.posteduser);
        comment = (TextView)findViewById(R.id.comment);
        back = (ImageView )findViewById(R.id.back);

        name.setText(fraudname);
        id.setText(".Transaction id : "+transactionid);
        wallet.setText(".Wallet name : "+walletname);
        number.setText(".Phone number : "+phonenumber);
        comment.setText(complaint);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayPhonedetails.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }
}
