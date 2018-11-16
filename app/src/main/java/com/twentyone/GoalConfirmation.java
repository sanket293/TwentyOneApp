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
    private String goalActionDate, goalEndDate = "";
    private int totalDaysOfGoal, goalCompletionDays, isGoalFinished;

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


        UserGoalList userGoalList = dataBaseHelper.getOneGoalRecord(goalId);

        if (userGoalList == null) {

            redirectToGoalListActivity(getResources().getString(R.string.err_pleaseTryAgain));
        }
        if (isGoalFinished == CommonFunctions.IS_GOAL_FINISHED) {
            Toast.makeText(context, getResources().getString(R.string.msg_GoalActionSuccessfully), Toast.LENGTH_SHORT).show();
            redirectToGoalListActivity(getResources().getString(R.string.err_pleaseTryAgain));
        }


        goalEndDate = userGoalList.getGoalEndDate().toString();
        goalActionDate = CommonFunctions.getCurrentDate(context);//  goal action date is same as current date

        totalDaysOfGoal = userGoalList.getTotalDaysOfGoal();
        goalCompletionDays = userGoalList.getGoalCompletionDays();
        isGoalFinished = userGoalList.getIsGoalFinished();

        tvGoalCompletedDays.setText(goalCompletionDays + "");


    }

    private void redirectToGoalListActivity(String message) {
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(context, GoalListActivity.class));
        finish();

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

        // todo check the condition if date is null or not


        UserGoalRecord goalRecord = new UserGoalRecord(goalId, name, goalAction, goalActionDate);

        if (dataBaseHelper.setGoalAction(goalRecord)) {
            if (goalAction == 1) {


                boolean flag = dataBaseHelper.updateGoalCompletionDays(goalId, goalCompletionDays + 1);// this method will update completion days in UserGoalList
                if (flag) {
                    tvGoalCompletedDays.setText(goalCompletionDays + "");
                    redirectToGoalListActivity(getResources().getString(R.string.msg_GoalActionSuccessfully));

                } else {

                    redirectToGoalListActivity(getResources().getString(R.string.err_pleaseTryAgain));

                }
            }
        } else {
            Toast.makeText(context, getResources().getString(R.string.err_pleaseTryAgain), Toast.LENGTH_SHORT).show();
        }

    }


    public void onCancelBtnClick(View view) {
        redirectToGoalListActivity("");


    }


}
