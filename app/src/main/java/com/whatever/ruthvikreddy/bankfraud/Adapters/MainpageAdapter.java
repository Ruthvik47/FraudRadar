package com.whatever.ruthvikreddy.bankfraud.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.whatever.ruthvikreddy.bankfraud.Fragments.AccountNumberSearch;
import com.whatever.ruthvikreddy.bankfraud.Fragments.PhoneSearch;
import com.whatever.ruthvikreddy.bankfraud.R;

public class MainpageAdapter extends FragmentPagerAdapter {
    private int[] tabIcons = {
            R.drawable.ic_local_phone,
            R.drawable.ic_account_balance,

    };


    public MainpageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        PhoneSearch phoneSearch = new PhoneSearch();
        AccountNumberSearch accountNumberSearch = new AccountNumberSearch();

        switch (i){
            case 0:
                return phoneSearch;
            case 1:
                return accountNumberSearch;
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Phone Search";
            case 1:
                return "Account number Search";
                default:
                    return null;

        }
    }
}
