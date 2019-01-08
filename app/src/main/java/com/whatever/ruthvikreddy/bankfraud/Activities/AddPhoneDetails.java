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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.whatever.ruthvikreddy.bankfraud.Model.PhoneDetails;
import com.whatever.ruthvikreddy.bankfraud.Model.sharedpreference;
import com.whatever.ruthvikreddy.bankfraud.R;

public class AddPhoneDetails extends AppCompatActivity {
    private static final String TAG = "AddPhoneDetails";
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private String userid;

    private ProgressBar progressBar;
    private MenuItem submit;
    private sharedpreference sharedpreference;

    private EditText fraudname,wallet,phonenumber,transactionid,description;
    private CheckBox anonymous;
    private boolean checkbox =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone_details);

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

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        fraudname = (EditText)findViewById(R.id.fraudname);
        wallet = (EditText)findViewById(R.id.walletname);
        phonenumber = (EditText)findViewById(R.id.phonenumber);
        transactionid = (EditText)findViewById(R.id.transactionid);
        description  = (EditText)findViewById(R.id.description);
        anonymous = (CheckBox)findViewById(R.id.anonymous);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu,menu);
        submit = menu.findItem(R.id.navigation_submit);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ((item.getItemId())){
            case R.id.navigation_submit:
                progressBar.setVisibility(View.VISIBLE);
                submit.setEnabled(false);

                final String fraud = fraudname.getText().toString();
                final String walletname = wallet.getText().toString();
                final String phoneno = phonenumber.getText().toString();
                final String transaction = transactionid.getText().toString();
                final String des = description.getText().toString();
                if(fraud.isEmpty() || walletname.isEmpty() || phoneno.isEmpty() || transaction.isEmpty() || des.isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddPhoneDetails.this,"Please fill the flieds",Toast.LENGTH_LONG).show();

                }else{
                    PhoneDetails phoneDetails = new PhoneDetails(fraud,walletname,phoneno,transaction,des,checkbox ? "Anonymous":sharedpreference.getUsername());
                    firestore.collection("PhoneSearch").add(phoneDetails).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                       Log.d(TAG,"uploaded successfully");
                            Intent intent = new Intent(AddPhoneDetails.this,DisplayPhonedetails.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("fraudname",fraud);
                            intent.putExtra("walletname",walletname);
                            intent.putExtra("transaction",transaction);
                            intent.putExtra("phonenumber",phoneno);
                            intent.putExtra("complaint",des);
                            intent.putExtra("postedby",checkbox ? "Anonymous":sharedpreference.getUsername());
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            submit.setEnabled(true);
                            Toast.makeText(AddPhoneDetails.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
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
