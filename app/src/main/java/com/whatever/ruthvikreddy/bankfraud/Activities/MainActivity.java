package com.whatever.ruthvikreddy.bankfraud.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.whatever.ruthvikreddy.bankfraud.Adapters.MainpageAdapter;
import com.whatever.ruthvikreddy.bankfraud.Model.ProfileDetails;
import com.whatever.ruthvikreddy.bankfraud.Model.sharedpreference;
import com.whatever.ruthvikreddy.bankfraud.R;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private String userid;
    private ViewPager viewPager;
    private MainpageAdapter mainpageAdapter;
    private TabLayout tabLayout;

    private FirebaseFirestore firestore;
    private sharedpreference sharedpreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        viewPager = (ViewPager) findViewById(R.id.mainPager);
        mainpageAdapter = new MainpageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainpageAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);



        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        sharedpreference = new sharedpreference(this);
        Log.d(TAG,sharedpreference.getUsername());
//userid = mAuth.getCurrentUser().getUid();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser == null){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else{
            userid = mAuth.getCurrentUser().getUid();
            String username = sharedpreference.getUsername();
            if(username.equals("") || username.isEmpty()){
                firestore.collection("Users").document(userid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(documentSnapshot!=null) {

                            ProfileDetails profileDetails = documentSnapshot.toObject(ProfileDetails.class);
                            if (profileDetails != null) {
                                String username = profileDetails.getFirstname() + " " + profileDetails.getLastname();
                                sharedpreference.writeUsername(username);
                                sharedpreference.writeUserphonenumber(profileDetails.getPhonenumber());
                            }
                        }

                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profilemenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.navigation_profile:
                Intent intent = new Intent(MainActivity.this,UserProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
