package com.twentyone;

        import android.app.AlarmManager;
        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.os.Bundle;
        import android.support.v4.app.NotificationCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;

public class NotificationActivity extends AppCompatActivity {
    //used for register alarm manager
    PendingIntent pendingIntent;
    //used to store running alarmmanager instance
    AlarmManager alarmManager;
    //Callback function for Alarmmanager event
    BroadcastReceiver mReceiver;
    private Context context = NotificationActivity.this;

    private NotificationHelper noti;
    int id = 1100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        noti = new NotificationHelper(this);
        // Finds you button from the xml layout file
        Button createNotificationButton = findViewById(R.id.button_create_notification);
        RegisterAlarmBroadcast();

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
            startActivity(new Intent(context, GoalListActivity.class));
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
                Notification.Builder nb = null;
                Log.i(TAG,"BroadcastReceiver::OnReceive() >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Toast.makeText(context, "Congrats!. Your Alarm time has been reached", Toast.LENGTH_LONG).show();
                nb = noti.getNotification1("Hi", "Bye");

                if (nb != null) {
                    noti.notify(id++, nb);
                }
                // addNotification();

            }
        };

        // register the alarm broadcast here
        registerReceiver(mReceiver, new IntentFilter("com.example.sam.notification") );
        pendingIntent = PendingIntent.getBroadcast( this, 0, new Intent("com.example.sam.notification"),0 );
        alarmManager = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
    }
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
    // Creates and displays a notification
    private void addNotification() {
        // Builds your notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("John's Android Studio Tutorials")
                .setContentText("A video has just arrived!");

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
