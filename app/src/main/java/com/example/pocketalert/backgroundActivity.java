package com.example.pocketalert;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.example.pocketalert.databaseHistory.History;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class backgroundActivity extends IntentService {
    private static final String ACTION_STRING_SERVICE = "ToService";
    private static final String ACTION_STRING_ACTIVITY = "ToActivity";
    private static final String ASK_FOR_DATA = "askforData";
    private static final String SEND_DATA = "sendData";
    private static final String SEND_DATA_SINGLE = "sendDataSingle";
    private static final String ACTION_STRING_ACTIVITY_SINGLE = "ToActivitySingle";

    private boolean sendUpdateHistories = false;
    public backgroundActivity() {
        super("backgroundActivity");
    }
    public static final String CHANNEL_1_ID = "notificationIfUserHasFallen";
    NotificationManagerCompat notiManCom;
    private ArrayList<History> histories = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
        notiManCom = NotificationManagerCompat.from(this);
        setupForeground();
        //STEP2: register the receiver
                if (serviceReceiver != null) {
        //Create an intent filter to listen to the broadcast sent with the action "ACTION_STRING_SERVICE"
                    IntentFilter intentFilter = new IntentFilter(ACTION_STRING_SERVICE);
        //Map the intent filter to the receiver
                    registerReceiver(serviceReceiver, intentFilter);
        }
    }
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Here will the work be done, potentially also receiving results
        // For now a for loop is used, this can be exchanged with other functions such as while loop
        // communication with server might happen here.

        for(int i =0; i < 3; i++) {
            // This sends a notification
            // sendNotificationOnChannel();

            History newHistory = new History(i,  "2-2-2021", "123", "Merlijn", 1.11111, 1.1111, false);
            sendHistory(newHistory);

            try {
                // waits 10 seconds
                Thread.sleep(10000);
            } catch (Exception e) {
                Log.d("exception",e.toString());
            }
        }
        // This must be removed on the final product!!
        stopSelf();
            }
    private void createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.user_notification_channel_name);
            String description = getString(R.string.user_notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_1_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceReceiver);
    }

    private void sendNotificationOnChannel(){
        int vibrationTime;
        SharedPreferences appSettingPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(appSettingPrefs.getBoolean("vibrationSwitchPreference",true)){
            vibrationTime = 1000;
        } else {
            vibrationTime = 0;
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_noti_icon)
                .setContentTitle(getString(R.string.user_notification_title))
                .setContentText(getString(R.string.user_notification_description))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVibrate(new long[] { vibrationTime, vibrationTime, vibrationTime, vibrationTime, vibrationTime})
                .build();
        notiManCom.notify(1,notification);
    }
    private void setupForeground(){
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        startForeground(1,notification);
    }
    private BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendUpdateHistories = intent.getBooleanExtra(ASK_FOR_DATA,false);
        }
    };
    //send broadcast from activity to all receivers listening to the action "ACTION_STRING_ACTIVITY"
    private void sendBroadcast(ArrayList<History> hist) {
        Intent new_intent = new Intent();
        new_intent.setAction(ACTION_STRING_ACTIVITY);
        Gson gson = new Gson();
        String histJson = gson.toJson(hist);
        new_intent.putExtra(SEND_DATA,histJson);
        sendBroadcast(new_intent);
    }
    private void sendBroadcastSingle(History hist) {
        Intent new_intent = new Intent();
        new_intent.setAction(ACTION_STRING_ACTIVITY_SINGLE);
        Gson gson = new Gson();
        String histJson = gson.toJson(hist);
        new_intent.putExtra(SEND_DATA_SINGLE,histJson);
        sendBroadcast(new_intent);
    }
    private void sendHistory(History h){
        histories.add(h);
        if(sendUpdateHistories) {
            sendBroadcast(histories);
            sendUpdateHistories = false;
        } else {
            sendBroadcastSingle(h);
        }
    }
}