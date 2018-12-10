package com.twentyone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }

    public void submitBtnClick(View view) {
        Toast.makeText(FeedbackActivity.this, "Thank You for your Feedback.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(FeedbackActivity.this, GoalListActivity.class));
        finish();

    }
}
