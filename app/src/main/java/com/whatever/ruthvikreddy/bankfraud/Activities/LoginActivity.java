package com.whatever.ruthvikreddy.bankfraud.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.whatever.ruthvikreddy.bankfraud.R;


import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private TextView enternum, tapnext,otpnumber;
    private EditText countrycode, phonenumber,otpcode;
    private Button next;
    private ProgressBar progressBar;
    private int buttontype = 0;

    //firebase
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        enternum = (TextView) findViewById(R.id.enternum);
        tapnext = (TextView) findViewById(R.id.verifytext);
        otpnumber = (TextView)findViewById(R.id.otpnumber);
        countrycode = (EditText) findViewById(R.id.countrycode);
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        otpcode = (EditText)findViewById(R.id.otpcode);
        next = (Button) findViewById(R.id.next);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        countrycode.setEnabled(false);

        mAuth = FirebaseAuth.getInstance();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              verifyPhoneNumber();
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);


                 signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                next.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }


            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);


                // Save verification ID and resending token so we can use them later
                otpnumber.setText("Enter the code sent to \n"+number);
                next.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                enternum.setVisibility(View.GONE);
                otpnumber.setVisibility(View.VISIBLE);
                countrycode.setVisibility(View.GONE);
                tapnext.setVisibility(View.GONE);
                phonenumber.setVisibility(View.GONE);
                otpcode.setVisibility(View.VISIBLE);
                buttontype = 1;
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LoginActivity.this,task.getException()+"",Toast.LENGTH_SHORT).show();
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void verifyPhoneNumber() {
        if(buttontype == 0){
            next.setText("Continue");
            next.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            number = phonenumber.getText().toString();
            if (!number.isEmpty()) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91"+number,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        LoginActivity.this,               // Activity (for callback binding)
                        mCallbacks);
            }
        }else if(buttontype == 1){
            String otp = otpcode.getText().toString();

            if(!otp.equals("")){
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,otp);
                signInWithPhoneAuthCredential(credential);
            }

        }
    }
}
