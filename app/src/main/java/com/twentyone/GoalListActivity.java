package com.twentyone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.twentyone.CommonFunctions.getUserName;

public class GoalListActivity extends AppCompatActivity {
    private DataBaseHelper dataBaseHelper;
    private Context context = GoalListActivity.this;
    private ListView lvGoalList;
    private String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_list);
        findId();
    }

    private void findId() {


        dataBaseHelper = DataBaseHelper.getInstance(context); //  create instance of db
        lvGoalList = (ListView) findViewById(R.id.lvGoalList);

        email = getUserName(context);

        List<UserGoalList> goalList = dataBaseHelper.getGoalList(email);
        if (goalList.size() > 0) {
            ListviewAdapter listviewAdapter = new ListviewAdapter(goalList);
            lvGoalList.setAdapter(listviewAdapter);
            }
    }

    public void onAddNewGoal(View view){
        startActivity(new Intent(context,AddGoalActivity.class));
        finish();
    }



    public class ListviewAdapter extends BaseAdapter {

        private List<UserGoalList> goalList = new ArrayList<>();

        public ListviewAdapter(List<UserGoalList> goalList) {
            this.goalList = goalList;
        }


        @Override
        public int getCount() {
            return goalList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.simple_text_adapter, parent, false);

                holder.tvTextAdapter = (TextView) convertView.findViewById(R.id.tvTextAdapter);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final String goalName=goalList.get(position).getGoal().toString();
            holder.tvTextAdapter.setText(goalName);

            final int goalId =goalList.get(position).getGoalId();

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivity(new Intent(context, GoalConfirmation.class).putExtra("goalId", goalId).putExtra("goalName",goalName));
                    } catch (Exception e) {
                        Log.e("error main row adapter", e.getMessage());
                    }
                }
            });

            return convertView;
        }
    }

    public class ViewHolder {
        TextView tvTextAdapter;
    }
}
