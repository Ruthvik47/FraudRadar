package com.whatever.ruthvikreddy.bankfraud.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.whatever.ruthvikreddy.bankfraud.R;

public class sharedpreference {
    private SharedPreferences sharedPreferences;
    private Context context;

    public sharedpreference(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.shared_pref),Context.MODE_PRIVATE);
        this.context = context;
    }


    /*
   stores user name
     */

    public void writeUsername(String username){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.username),username);
        editor.apply();
    }
    public  String getUsername(){
        String username = "";
        username = sharedPreferences.getString(context.getResources().getString(R.string.username),username);
        return username;
    }
    /* --*/
    /*

     */
       /*
   stores user name
     */

    public void writeUserphonenumber(String phonenumber){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.phonenumber),phonenumber);
        editor.apply();
    }
    public  String getUserphonenumber(){
        String phonenumber = "";
        phonenumber = sharedPreferences.getString(context.getResources().getString(R.string.phonenumber),phonenumber);
        return phonenumber;
    }
    /* --*/
    /*

     */
}
