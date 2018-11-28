package com.twentyone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.twentyone.LoginActivity.LOGIN_PREFERENCES;

public class AddGoalActivity extends AppCompatActivity {
    private EditText etSetGoal, etToalDaysForGoal;
    private DataBaseHelper dataBaseHelper;
    private Context context = AddGoalActivity.this;
    private SharedPreferences sharedpreferences;
    private UserFields userFields = null;
    private String email = "";
    private String preGoalString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        findId();
        populateSpinner();
    }

    private void findId() {

        dataBaseHelper = DataBaseHelper.getInstance(context); //  create instance of db
        etSetGoal = (EditText) findViewById(R.id.etSetGoal);
        etToalDaysForGoal = (EditText) findViewById(R.id.etToalDaysForGoal);


        sharedpreferences = getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();


        email = sharedpreferences.getString(LoginActivity.USERNAME_KEY, "");

        if (email.equalsIgnoreCase("")) { // if no user found or something went wrong it will go to login screen

            editor.clear().commit();
            Toast.makeText(context, getResources().getString(R.string.err_pleaseTryAgain), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }


    }

    //populate Spinner
    private void populateSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter ;
        List<PredefinedGoals> list = new ArrayList<>();
        list = dataBaseHelper.getPreGoal();
        List<String> list1 = new ArrayList<String>();
        list1.add("Please Select Goal or Write Below");
        for(int i=0;i<list.size();i++) {

            String pGoal = list.get(i).getPreGoal().toString();
            list1.add(pGoal);
        }

        adapter = new ArrayAdapter<String>(AddGoalActivity.this,
                android.R.layout.simple_spinner_item, list1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position,long id) {
                 preGoalString = parent.getItemAtPosition(position).toString();
                Toast.makeText(context,"selected item: "+preGoalString+""
                        , Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void onSetGoalBtnClick(View view) {


        String setGoalStr = etSetGoal.getText().toString().trim();
        String totalDaysStr = etToalDaysForGoal.getText().toString().trim();
        if (setGoalStr.equalsIgnoreCase("")) {
            if(preGoalString.equalsIgnoreCase("") ||
                    preGoalString.equalsIgnoreCase("Please Select Goal or Write Below"))
            {
            Toast.makeText(context, getResources().getString(R.string.err_PleaseAddGoal)
                    , Toast.LENGTH_SHORT).show();

            return;
            }
            else
            {
                setGoalStr = preGoalString;
            }
        }

        if (totalDaysStr.equalsIgnoreCase("")) {
            Toast.makeText(context, getResources().getString(R.string.err_PleaseAddGoal)
                    , Toast.LENGTH_SHORT).show();

            return;
        }
        int totalDays = Integer.parseInt(totalDaysStr);


        String goalCreationDate = CommonFunctions.getCurrentDate(context);

        String goalEndDate = CommonFunctions.getCustomeDate(context, goalCreationDate, totalDaysStr);
        UserGoalList userGoalList = new UserGoalList();
        userGoalList.setGoal(setGoalStr);
        userGoalList.setTotalDaysOfGoal(totalDays);
        userGoalList.setName(email);
        userGoalList.setIsGoalFinished(0);
        userGoalList.setGoalCreatedDate(goalCreationDate);
        userGoalList.setGoalEndDate(goalEndDate);


        if (dataBaseHelper.addGoalString(userGoalList)) {


            Toast.makeText(context, getResources().getString(R.string.msg_GoalSuccessfully)
                    , Toast.LENGTH_SHORT).show();


            startActivity(new Intent(context, GoalListActivity.class));
            etSetGoal.setText("");
            finish();


        } else {

            Toast.makeText(context, getResources().getString(R.string.err_pleaseTryAgain)
                    , Toast.LENGTH_SHORT).show();
        }

    }


    public void onCancelbtnClick(View view) {


    }

}