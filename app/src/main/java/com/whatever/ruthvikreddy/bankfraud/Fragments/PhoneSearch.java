package com.whatever.ruthvikreddy.bankfraud.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.whatever.ruthvikreddy.bankfraud.Activities.AddAccountDetails;
import com.whatever.ruthvikreddy.bankfraud.Activities.AddPhoneDetails;
import com.whatever.ruthvikreddy.bankfraud.Adapters.PhoneSearchAdapter;
import com.whatever.ruthvikreddy.bankfraud.Model.PhoneDetails;
import com.whatever.ruthvikreddy.bankfraud.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhoneSearch extends Fragment {
    private static final String TAG = "PhoneSearch";

    private EditText searchEdit;
    private ImageView search;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private String userid;


    private Dialog nodetails;
    private TextView nodata,results;
    private Button adddetails;
    private AdddetailsFragment fragment;

    private PhoneSearchAdapter phoneSearchAdapter;
    private RecyclerView phonerecycler;
    private List<PhoneDetails> ListphoneDetails;
    private FloatingActionButton fab;


    public PhoneSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_phone_search, container, false);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        searchEdit = (EditText)view.findViewById(R.id.searchedittext);
        search = (ImageView)view.findViewById(R.id.search);
        results = (TextView)view.findViewById(R.id.results);

        phonerecycler = (RecyclerView)view.findViewById(R.id.phonerecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ListphoneDetails = new ArrayList<>();
        phoneSearchAdapter = new PhoneSearchAdapter(getContext(),ListphoneDetails);
        phonerecycler.setLayoutManager(linearLayoutManager);
        phonerecycler.setAdapter(phoneSearchAdapter);

        nodetails = new Dialog(getContext());

        fab = (FloatingActionButton)view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddPhoneDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                    hideKeyboard(v);

                ListphoneDetails.clear();
                final String phonenumber = searchEdit.getText().toString();

                firestore.collection("PhoneSearch").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(queryDocumentSnapshots != null){
                            for(DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()){
                                results.setVisibility(View.VISIBLE);

                                PhoneDetails phoneDetails = documentChange.getDocument().toObject(PhoneDetails.class);
                                String phonenum =phoneDetails.getPhonenumber();
                                if(phonenum.equals(phonenumber)){
                                    Log.d(TAG,""+phonenum);
                                    PhoneDetails details = documentChange.getDocument().toObject(PhoneDetails.class);
                                    Log.d(TAG,details+"");
                                    ListphoneDetails.add(details);


                                }else{
//
                                }
                                phoneSearchAdapter.notifyDataSetChanged();


                            }

                            if(ListphoneDetails.isEmpty() || ListphoneDetails == null){
                                Log.d(TAG,"empty");
                                results.setVisibility(View.VISIBLE);
                                nodetails.setContentView(R.layout.nodatalayout);
                                    nodata = (TextView)nodetails.findViewById(R.id.nodata);
                                    adddetails = (Button)nodetails.findViewById(R.id.addDetails);

                                    adddetails.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            nodetails.dismiss();
                                            Intent intent = new Intent(getContext(),AddPhoneDetails.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                        }
                                    });
                                    Log.d(TAG,"No user ");
                                    nodetails.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    nodetails.show();
                            }else{
                                results.setText(ListphoneDetails.size()+" results found");
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
