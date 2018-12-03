package com.whatever.ruthvikreddy.bankfraud.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.whatever.ruthvikreddy.bankfraud.Model.Bankcomments;
import com.whatever.ruthvikreddy.bankfraud.Model.Bankdetails;
import com.whatever.ruthvikreddy.bankfraud.Model.sharedpreference;
import com.whatever.ruthvikreddy.bankfraud.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class AddAccountDetails extends AppCompatActivity {
    private static final String TAG = "AddAccountDetails";
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private String userid;

    private Dialog nodetails;
    private TextView data;
    private Button viewdetails;

    private EditText fraudname,bank,accountnumber,ifsc,transactionid,description;
    private CheckBox anonymous;
    private boolean checkbox =false;
    private sharedpreference sharedpreference;
    private List<Bankdetails> bankdetailsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account_details);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();
        sharedpreference = new sharedpreference(this);

        fraudname = (EditText)findViewById(R.id.fraudname);
        bank = (EditText)findViewById(R.id.bankname);
        accountnumber = (EditText)findViewById(R.id.accountnumber);
        ifsc = (EditText)findViewById(R.id.ifscnumber);
        transactionid = (EditText)findViewById(R.id.transactionid);
        description  = (EditText)findViewById(R.id.description);
        anonymous = (CheckBox)findViewById(R.id.anonymous);

        nodetails = new Dialog(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ((item.getItemId())) {
            case R.id.navigation_submit:
                String fraud = fraudname.getText().toString();
                String bankname = bank.getText().toString();
                final String accountno = accountnumber.getText().toString();
                String ifscnum = ifsc.getText().toString();
                String transaction = transactionid.getText().toString();
                final String des = description.getText().toString();
                if(fraud.isEmpty() || bankname.isEmpty() || accountno.isEmpty() || transaction.isEmpty() || des.isEmpty()|| ifscnum.isEmpty()){

                }else{
                    final Bankdetails bankdetails = new Bankdetails(fraud,bankname,accountno,ifscnum);
                    firestore.collection("AccountSearch").document(accountno).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            final Bankdetails details = task.getResult().toObject(Bankdetails.class);
                            if(details == null){
                                Log.d(TAG,"2 their is no record");
                                firestore.collection("AccountSearch").document(accountno).set(bankdetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG,"added");
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Bankcomments bankcomments = new Bankcomments(des,checkbox?"Anonymous":sharedpreference.getUsername(),Timestamp.now());
                                        firestore.collection("AccountSearch").document(accountno).collection("Comments")
                                        .add(bankcomments);
                                    }
                                });
                            }else{
                                Log.d(TAG,"2 already their is a record");
                                nodetails.setContentView(R.layout.viewdatalayout);
                                data = (TextView)nodetails.findViewById(R.id.nodata);
                                viewdetails = (Button)nodetails.findViewById(R.id.viewdetails);

                                viewdetails.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        nodetails.dismiss();
                                        Intent intent = new Intent(AddAccountDetails.this,DisplayBankDetails.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("fraudname",details.getFraudname());
                                        intent.putExtra("accoutnumber",details.getAccountnumber());
                                        intent.putExtra("ifscnumber",details.getIfscnumber());
                                        intent.putExtra("bankname",details.getBankname());
                                        startActivity(intent);

                                    }
                                });
                                Log.d(TAG,"No user ");
                                nodetails.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                nodetails.show();
                            }
                            if (task.getResult().exists()) {

                            }else {


                            }                        }
                    });

//
//                    firestore.collection("AccountSearch").addSnapshotListener(new EventListener<QuerySnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                            if(queryDocumentSnapshots!=null){
//                                for(DocumentChange change : queryDocumentSnapshots.getDocumentChanges()){
//                                    Bankdetails bank = change.getDocument().toObject(Bankdetails.class);
//                                    String account = bank.getAccountnumber();
//                                        if(account.equals(accountno)){
//                                            Bankdetails bankdetails1 = change.getDocument().toObject(Bankdetails.class);
//                                            bankdetailsList.add(bankdetails1);
//                                            Log.d(TAG,""+bankdetailsList);
//                                        }
//
//                                }
//                                    if(bankdetailsList == null || bankdetailsList == null){
//                                        firestore.collection("AccountSearch").document(accountno).set(bankdetails).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                Log.d(TAG,"added");
//                                            }
//                                        });
//                                    }else{
//                                        Log.d(TAG,"already their is a record");
//                                    }
//
//
//                            }
//                        }
//                    });


//                    firestore.collection("AccountSearch").document(accountno).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                            if (documentSnapshot != null) {
//                                if(documentSnapshot.exists()){
//
//                                }else{
//
//                                }
//                            }
//                        }
//                    });

                }
                break;
                }
        return super.onOptionsItemSelected(item);
    }
    public void oncheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.anonymous:
                if (checked){
                    checkbox =true;
                }
                // Put some meat on the sandwich
                else {
                    checkbox = false;
                }
                break;
        }
    }
}
