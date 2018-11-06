package com.twentyone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import static com.twentyone.LoginActivity.LOGIN_PREFERENCES;

public class CommonFunctions {
    public static SharedPreferences sharedpreferences;
    public static String email = "";

    public static String getUserName(Context context) {
        sharedpreferences = context.getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        email = sharedpreferences.getString(LoginActivity.USERNAME_KEY, "");

        if (email.equalsIgnoreCase("")) { // if no user found or something went wrong it will go to login screen
            editor.clear().commit();
            Toast.makeText(context, context.getResources().getString(R.string.err_pleaseTryAgain), Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context.getApplicationContext(), LoginActivity.class));

        }

        return email;

    }


}
