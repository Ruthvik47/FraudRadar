package com.whatever.ruthvikreddy.bankfraud.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.whatever.ruthvikreddy.bankfraud.Model.Bankcomments;
import com.whatever.ruthvikreddy.bankfraud.Model.PhoneDetails;
import com.whatever.ruthvikreddy.bankfraud.Model.sharedpreference;
import com.whatever.ruthvikreddy.bankfraud.R;

import java.util.HashMap;
import java.util.Map;

public class Fraudbankdetailscomments extends AppCompatActivity {
    private static final String TAG = "Fraudbankdetaikscomment";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private EditText message;
    private String userid;
    private sharedpreference sharedpreference;
    private boolean checkbox =false;
    private String accountnumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fraudbankdetailscomments);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        sharedpreference = new sharedpreference(this);

        accountnumber = getIntent().getStringExtra("accountnumber");

        message = (EditText)findViewById(R.id.comment);

        firestore= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ((item.getItemId())){
            case R.id.navigation_submit:
            String msg = message.getText().toString();

            if(!msg.isEmpty()){
                Bankcomments bankcomments = new Bankcomments(msg, checkbox ? "Anonymously" : sharedpreference.getUsername(),Timestamp.now());
                firestore.collection("AccountSearch").document(accountnumber).collection("Comments").add(bankcomments).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,"comment added sucessfully");
                    }
                });
            }
            break;

            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void oncheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.anonymous:
                // Put some meat on the sandwich
                checkbox = checked;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Fraudbankdetailscomments.this,DisplayBankDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("fraudname",getIntent().getStringExtra("fraudname"));
        intent.putExtra("ifscnumber",getIntent().getStringExtra("ifscnumber"));
        intent.putExtra("bankname",getIntent().getStringExtra("bankname"));
        intent.putExtra("accountnumber", accountnumber);
        startActivity(intent);
    }
}
