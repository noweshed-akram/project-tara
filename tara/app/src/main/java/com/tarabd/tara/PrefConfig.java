package com.tarabd.tara;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class PrefConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public PrefConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);
    }

    public void writeLoginStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_customer_login_status), status);
        editor.apply();
    }

    public void writeMerchantLoginStatus(boolean ownerStatus) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_merchant_login_status), ownerStatus);
        editor.apply();
    }

    public boolean readloginstatus() {
        return sharedPreferences.getBoolean(context.getString(R.string.pref_customer_login_status), false);
    }

    public boolean readmerchantloginstatus() {
        return sharedPreferences.getBoolean(context.getString(R.string.pref_merchant_login_status), false);
    }

    public void writename(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_name), name);
        editor.apply();
    }

    public void writenumber(String number) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_number), number);
        editor.apply();
    }

    public void writeemail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_email), email);
        editor.apply();
    }

    public void writeaddress(String address) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_address), address);
        editor.apply();
    }

    public void writeProfileUrl(String profileurl) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_profile_url), profileurl);
        editor.apply();
    }

    public String readProfileUrl() {
        return sharedPreferences.getString(context.getString(R.string.pref_user_profile_url), "url");
    }

    public String readName() {
        return sharedPreferences.getString(context.getString(R.string.pref_user_name), "User");
    }

    public String readNumber() {
        return sharedPreferences.getString(context.getString(R.string.pref_user_number), "01*********");
    }

    public String readEmail() {
        return sharedPreferences.getString(context.getString(R.string.pref_user_email), "Email");
    }

    public String readAddress() {
        return sharedPreferences.getString(context.getString(R.string.pref_user_address), "Address");
    }

    public void displaytoast(String messages) {
        Toast.makeText(context, messages, Toast.LENGTH_LONG).show();
    }
}
