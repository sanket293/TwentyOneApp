package com.twentyone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AdminQuoteDeleteActivity extends AppCompatActivity {

    private EditText etQuoteChange;
    private DataBaseHelper dataBaseHelper;
    private Context context = AdminQuoteDeleteActivity.this;
    private int quoteId = -1;
    private String quote = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quote_delete);
        findId();
    }

    private void findId() {
        dataBaseHelper = DataBaseHelper.getInstance(context); //  create instance of db

        etQuoteChange = (EditText) findViewById(R.id.etQuoteChange);

        Intent intent = getIntent();
        quoteId =
                intent.getIntExtra("quoteId", -1);

        if (quoteId == -1) { // if it's -1 , something wrong happened

            redirectToBackActivity(getResources().getString(R.string.err_pleaseTryAgain));
        }
        quote = etQuoteChange.getText().toString();


    }


    public void onUpdateQuotesBtnClick(View view) {
        if (isValidQuoteString()) {
            if (dataBaseHelper.updateQuotes(quoteId)) {

                redirectToBackActivity(getResources().getString(R.string.msg_QuoteUpdatedSuccessfully));
            } else {
                Toast.makeText(context, getResources().getString(R.string.err_pleaseTryAgain), Toast.LENGTH_SHORT).show();

            }

        }
    }


    public void onDeleteQuotesBtnClick(View view) {
        if (isValidQuoteString()) {
            if (dataBaseHelper.deleteQuotes(quoteId)) {
                redirectToBackActivity(getResources().getString(R.string.msg_QuoteDeletedSuccessfully));
            } else {
                Toast.makeText(context, getResources().getString(R.string.err_pleaseTryAgain), Toast.LENGTH_SHORT).show();

            }

        }
    }

    public void onCancelbtnClick(View view) {
        redirectToBackActivity("");
    }

    private boolean isValidQuoteString() {
        if (quote.equalsIgnoreCase("")) {
            redirectToBackActivity(getResources().getString(R.string.err_pleaseTryAgain));
        }
        return true;
    }

    private void redirectToBackActivity(String message) {
        Toast.makeText(context, message + " ", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(context, AdminAddQuotesActivity.class));
        finish();
    }

}

