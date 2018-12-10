package com.twentyone;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.twentyone.CommonFunctions.getUserName;

public class GoalListActivity extends AppCompatActivity {
    private DataBaseHelper dataBaseHelper;
    private Context context = GoalListActivity.this;
    private ListView lvGoalList;
    private String email = "";

    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    BroadcastReceiver mReceiver;
    private NotificationHelper noti;
    int id = 1100;
    int id2 = 1300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goal_list);
        RegisterAlarmBroadcast();
        findId();
        noti = new NotificationHelper(this);
        Button createNotificationButton = findViewById(R.id.button_create_notification);


        // Waits for you to click the button
        createNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmManager.set( AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 , pendingIntent );
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000 , pendingIntent);
                // Starts the function below

            }
        });

    }



    private void setupToolBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.YourGoalList));
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_users, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (menuItem.getItemId() == R.id.ic_logout) {

            Toast.makeText(context, getResources().getString(R.string.LogOut), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context, LoginActivity.class));
            finish();
            // TODO set null value to sp

        }
        if (menuItem.getItemId() == R.id.rating) {

            Toast.makeText(context, getResources().getString(R.string.Cancel), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context, NotificationActivity.class));
            finish();
            // TODO set null value to sp

        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void RegisterAlarmBroadcast()
    {


        //This is the call back function(BroadcastReceiver) which will be call when your
        //alarm time will reached.
        mReceiver = new BroadcastReceiver()
        {
            private static final String TAG = "Alarm Example Receiver";
            @Override
            public void onReceive(Context context, Intent intent)
            {
                List<Quotes> list = new ArrayList<>();
                list = dataBaseHelper.getQuotes();
                Random r = new Random();
                int i = r.nextInt(list.size()) + 0;
                String dailyQuote = list.get(i).getQuotes().toString();
                Notification.Builder nb = null;
                Notification.Builder nb1 = null;
                Log.i(TAG,"BroadcastReceiver::OnReceive() >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Toast.makeText(context, "Congrats!. Your Alarm time has been reached", Toast.LENGTH_LONG).show();
                nb = noti.getNotification1("Daily Reminder", "Open the app and check your Goal");
                nb1 = noti.getNotification2("Daily Quote", dailyQuote);

                if (nb != null) {
                    noti.notify(id++, nb);
                }
                if (nb1 != null) {
                    noti.notify(id2++, nb1);
                }
                // addNotification();

            }
        };

        // register the alarm broadcast here
        registerReceiver(mReceiver, new IntentFilter("com.twentyone") );
        pendingIntent = PendingIntent.getBroadcast( this, 0, new Intent("com.twentyone"),0 );
        alarmManager = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
    }
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(context, LoginActivity.class));
        finish();
        super.onBackPressed();
    }





    private void findId() {
        setupToolBar();
        dataBaseHelper = DataBaseHelper.getInstance(context); //  create instance of db
        lvGoalList = (ListView) findViewById(R.id.lvGoalList);

        email = getUserName(context);
        List<UserGoalList> goalList = dataBaseHelper.getGoalList(email);

        if (goalList.size() > 0) {
            ListviewAdapter listviewAdapter = new ListviewAdapter(goalList);
            lvGoalList.setAdapter(listviewAdapter);
        }


    }



    public void onAddNewGoal(View view) {
        startActivity(new Intent(context, AddGoalActivity.class));
        finish();
    }

    public void onViewRecords(View view) {
        startActivity(new Intent(context, UserRecordsActivity.class));
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

            final String goalName = goalList.get(position).getGoal().toString();
            holder.tvTextAdapter.setText(goalName);

            //todo

            final int goalId = goalList.get(position).getGoalId();

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivity(new Intent(context, GoalConfirmation.class).putExtra("goalId", goalId).putExtra("goalName", goalName));
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
