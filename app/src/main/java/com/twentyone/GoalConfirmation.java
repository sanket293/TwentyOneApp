package com.twentyone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GoalConfirmation extends AppCompatActivity {
    private DataBaseHelper dataBaseHelper;
    private Context context = GoalConfirmation.this;
    private int goalId = -1;
    private String name = "", goalName = "";
    private TextView tvGoalOfUser, tvGoalCompletedDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_confirmation);

        findId();


    }

    private void findId() {

        Intent intent = getIntent();
        goalId = intent.getIntExtra("goalId", -1);
        goalName = intent.getStringExtra("goalName");

        name = CommonFunctions.getUserName(context);

        if (goalId == -1) { // if it's -1 , something wrong happened
            startActivity(new Intent(GoalConfirmation.this, GoalListActivity.class));
            finish();
        }

        if (name.equalsIgnoreCase("")) {
            startActivity(new Intent(GoalConfirmation.this, GoalListActivity.class));
            finish();
        }

        dataBaseHelper = DataBaseHelper.getInstance(context); //  create instance of db

        tvGoalOfUser = (TextView) findViewById(R.id.tvGoalOfUser);
        tvGoalCompletedDays = (TextView) findViewById(R.id.tvGoalCompletedDays);

        if (!goalName.equalsIgnoreCase("")) {
            tvGoalOfUser.setText(goalName);
        }


//        UserGoalRecord goalRecord = dataBaseHelper.getGoalRecord(goalId);


    }


    public void onYesBtnClick(View view) {

        didUserFinishedGoal(true);
    }

    public void onNoBtnClick(View view) {
        didUserFinishedGoal(false);
    }

    // after pressing yes or no button, this method will invoke
    private void didUserFinishedGoal(boolean isGoalCompleted) {

        int goalAction = 0;
        if (isGoalCompleted) {
            goalAction = 1;
        }
        String goalActionDate = CommonFunctions.getCurrentDate(context);
        // todo check the condition if date is null or not

        UserGoalRecord goalRecord = new UserGoalRecord(goalId, name, goalAction, goalActionDate);

        if (dataBaseHelper.setGoalAction(goalRecord)) {
            if(goalAction==1) {
       //todo          dataBaseHelper.updateGoalCompletionDays(1);
                }
            Toast.makeText(context, getResources().getString(R.string.msg_GoalActionSuccessfully), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context, GoalListActivity.class));
            finish();

        } else {
            Toast.makeText(context, getResources().getString(R.string.err_pleaseTryAgain), Toast.LENGTH_SHORT).show();
        }

    }



    public void onCancelBtnClick(View view) {

        startActivity(new Intent(context, GoalListActivity.class));
        finish();
    }


}
