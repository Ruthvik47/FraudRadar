package com.whatever.ruthvikreddy.bankfraud.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.whatever.ruthvikreddy.bankfraud.Model.ProfileDetails;
import com.whatever.ruthvikreddy.bankfraud.Model.sharedpreference;
import com.whatever.ruthvikreddy.bankfraud.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity";
    private CircleImageView profile;
    private TextView username,phonenumber,email;
    private FirebaseAuth mAuth;;
    private FirebaseFirestore firestore;
    private String userid;
    private ProgressBar progressbar;

    private sharedpreference sharedpreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sharedpreference = new sharedpreference(this);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();
        Log.d(TAG,userid);

        username = (TextView)findViewById(R.id.name);
        phonenumber = (TextView)findViewById(R.id.usernumber);
        email = (TextView)findViewById(R.id.useremail);
        profile = (CircleImageView)findViewById(R.id.profile);
        progressbar = (ProgressBar)findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);


        firestore.document("Users/"+userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot != null){
                    ProfileDetails profileDetails = documentSnapshot.toObject(ProfileDetails.class);
                    if(profileDetails!=null) {
                        progressbar.setVisibility(View.GONE);
                        username.setText(profileDetails.getFirstname() + " " + profileDetails.getLastname());
                        email.setText(profileDetails.getEmail());
                        String number = profileDetails.getPhonenumber();
                        if(number.isEmpty()){
                            phonenumber.setText(sharedpreference.getUserphonenumber());
                        }else{
                            phonenumber.setText(number);
                        }
                        String photourl = profileDetails.getPhotourl();
                        if(!photourl.equals("noimage")){
                            Picasso.get().load(Uri.parse(photourl)).noFade().into(profile);
                        }
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Intent intent = new Intent(UserProfileActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(UserProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
