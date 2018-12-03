package com.whatever.ruthvikreddy.bankfraud.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whatever.ruthvikreddy.bankfraud.R;

public class AdddetailsFragment extends BottomSheetDialogFragment {
    private Context context;
    public AdddetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.addfrauddetails,container,false);

        return view;

    }

    public interface senddata{

    }
}
