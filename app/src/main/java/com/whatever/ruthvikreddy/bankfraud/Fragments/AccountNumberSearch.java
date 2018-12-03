package com.whatever.ruthvikreddy.bankfraud.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.whatever.ruthvikreddy.bankfraud.Activities.AddAccountDetails;
import com.whatever.ruthvikreddy.bankfraud.Activities.AddPhoneDetails;
import com.whatever.ruthvikreddy.bankfraud.Activities.LoginActivity;
import com.whatever.ruthvikreddy.bankfraud.Activities.MainActivity;
import com.whatever.ruthvikreddy.bankfraud.Adapters.AccountSearchAdapter;
import com.whatever.ruthvikreddy.bankfraud.Model.Bankdetails;
import com.whatever.ruthvikreddy.bankfraud.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountNumberSearch extends Fragment {
    private static final String TAG = "AccountNumberSearch";
    private EditText searchEdit;
    private ImageView search;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private String userid;


    private Dialog nodetails;
    private TextView nodata,results;
    private Button adddetails;
    private AdddetailsFragment fragment;
    private FloatingActionButton fab;

    private RecyclerView recyclerView;
    private AccountSearchAdapter accountSearchAdapter;
    private List<Bankdetails> Listbankdetails;

    public AccountNumberSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_number_search, container, false);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        searchEdit = (EditText)view.findViewById(R.id.searchedittext);
        search = (ImageView)view.findViewById(R.id.search);
        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        results = (TextView)view.findViewById(R.id.results);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddAccountDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        nodetails = new Dialog(getContext());

        Listbankdetails = new ArrayList<>();
        accountSearchAdapter = new AccountSearchAdapter(getContext(),Listbankdetails);
        recyclerView = (RecyclerView)view.findViewById(R.id.searchrecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(accountSearchAdapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listbankdetails.clear();
                hideKeyboard(v);
                final String accountNumber =searchEdit.getText().toString();
                firestore.collection("AccountSearch").document(accountNumber).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(documentSnapshot!=null){
                            Bankdetails bankdetails = documentSnapshot.toObject(Bankdetails.class);
                            if(bankdetails != null){
                                results.setVisibility(View.VISIBLE);;
                                String accountnum = bankdetails.getAccountnumber();
                                if(accountNumber.equals(accountnum)){
                                    Bankdetails details = documentSnapshot.toObject(Bankdetails.class);
                                    Log.d(TAG,details+"");
                                    Listbankdetails.add(details);

                                }
                                accountSearchAdapter.notifyDataSetChanged();
                                results.setText(Listbankdetails.size()+" result(s) found");
                            }else{
                                results.setVisibility(View.VISIBLE);
                                Log.d(TAG,"empty");
                                nodetails.setContentView(R.layout.nodatalayout);
                                nodata = (TextView)nodetails.findViewById(R.id.nodata);
                                adddetails = (Button)nodetails.findViewById(R.id.addDetails);

                                adddetails.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        nodetails.dismiss();
                                        Intent intent = new Intent(getContext(),AddAccountDetails.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);

                                    }
                                });
                                Log.d(TAG,"No user ");
                                nodetails.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                nodetails.show();
                            }

                        }
                    }
                });
            }
        });


        return view;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
