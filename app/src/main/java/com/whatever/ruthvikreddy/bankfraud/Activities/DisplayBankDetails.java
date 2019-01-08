package com.whatever.ruthvikreddy.bankfraud.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.whatever.ruthvikreddy.bankfraud.Adapters.BankCommentsAdapter;
import com.whatever.ruthvikreddy.bankfraud.Model.Bankcomments;
import com.whatever.ruthvikreddy.bankfraud.Model.Bankdetails;
import com.whatever.ruthvikreddy.bankfraud.Model.sharedpreference;
import com.whatever.ruthvikreddy.bankfraud.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class DisplayBankDetails extends AppCompatActivity {
    private TextView fraudname,acno,ifsc,bankname,spam;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String userid;
    private boolean anonymous = false;
    private sharedpreference sharedpreference;
    private FloatingActionButton fab;
    private ImageView back;
    private ProgressBar progressBar;

    private String accountno;

    private RecyclerView recyclerView;
    private List<Bankcomments> ListBankcomments;
    private BankCommentsAdapter bankCommentsAdapter;
    private static final  String TAG = "DisplayBankDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bank_details);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Details");
        }
        sharedpreference = new sharedpreference(this);

        accountno = getIntent().getStringExtra("accountnumber");
        Log.d(TAG,accountno);



        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        fraudname = (TextView)findViewById(R.id.fraudname);
        acno = (TextView)findViewById(R.id.accountnumber);
        ifsc = (TextView)findViewById(R.id.ifscnumber);
        bankname = (TextView)findViewById(R.id.bankname);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        back = (ImageView)findViewById(R.id.back);
        spam = (TextView)findViewById(R.id.spam);

        ListBankcomments = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView)findViewById(R.id.commentsrecycler);
        bankCommentsAdapter = new BankCommentsAdapter(this,ListBankcomments);
        recyclerView.setAdapter(bankCommentsAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        getComments();
        displayFraudDetails();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayBankDetails.this,Fraudbankdetailscomments.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("fraudname",getIntent().getStringExtra("fraudname"));
                intent.putExtra("ifscnumber",getIntent().getStringExtra("ifscnumber"));
                intent.putExtra("bankname",getIntent().getStringExtra("bankname"));
                intent.putExtra("accountnumber",accountno);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayBankDetails.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });





    }

    private void displayFraudDetails() {
        firestore.document("AccountSearch/"+accountno).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot!=null){
                    Bankdetails bankdetails = documentSnapshot.toObject(Bankdetails.class);

                    String fraud = null;
                    if (bankdetails != null) {
                        progressBar.setVisibility(View.GONE);
                        fraud = bankdetails.getFraudname();
                        String capFraud = fraud.substring(0,1).toUpperCase() + fraud.substring(1).toLowerCase();
                        fraudname.setText(capFraud);
                        acno.setText(".Account number : "+accountno);
                        ifsc.setText(".IFSC number : "+bankdetails.getIfscnumber());
                        bankname.setText(".Bank name :"+bankdetails.getBankname());
                    }


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DisplayBankDetails.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getComments() {
        firestore.collection("AccountSearch/"+accountno+"/Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null){
                    spam.setText(queryDocumentSnapshots.size() +" spam reports");
                    for(DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()){
                        Bankcomments bankcomments = documentChange.getDocument().toObject(Bankcomments.class);
                        ListBankcomments.add(bankcomments);
                    }
                bankCommentsAdapter.notifyDataSetChanged();
                    if(ListBankcomments == null || ListBankcomments.isEmpty()){
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }
        });

    }
}
