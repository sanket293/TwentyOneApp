package com.twentyone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class GoalConfirmation extends AppCompatActivity {
    private DataBaseHelper dataBaseHelper;
    private Context context = GoalConfirmation.this;
    private int goalId = -1;
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_confirmation);




        Intent intent = getIntent();
        goalId = intent.getIntExtra("goalId", -1);

        Toast.makeText(this, "position " + goalId, Toast.LENGTH_SHORT).show();

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

//        UserGoalRecord goalRecord = dataBaseHelper.getGoalRecord(goalId);


    }


    public void onYesBtnClick(View view) {

        didUserFinishedGoal(true);
    }

    public void onNoBtnClick(View view) {
        didUserFinishedGoal(false);
    }

    private void didUserFinishedGoal(boolean isGoalCompleted) {

        int goalAction = 0;
        if (isGoalCompleted) {
            goalAction = 1;
        }
        String goalActionDate = getCurrentDate();
        // todo check the condition if date is null or not

        UserGoalRecord goalRecord = new UserGoalRecord(goalId,name,goalAction,goalActionDate);

        if (dataBaseHelper.setGoalAction(goalRecord)) {
            Toast.makeText(context, getResources().getString(R.string.msg_GoalActionSuccessfully), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context, GoalListActivity.class));
            finish();

        } else {
            Toast.makeText(context, getResources().getString(R.string.err_pleaseTryAgain), Toast.LENGTH_SHORT).show();
        }

    }

    private String getCurrentDate() {

//todo return current date
        return "s";
    }


    public void onCancelBtnClick(View view) {

        startActivity(new Intent(context, GoalListActivity.class));
        finish();
    }


}
