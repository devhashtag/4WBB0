package com.example.pocketalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pocketalert.databaseHistory.History;
import com.example.pocketalert.databaseHistory.HistoryAdapter;
import com.example.pocketalert.databaseHistory.HistoryViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.provider.ContactsContract.Intents.Insert.ACTION;

public class HistoryActivity extends AppCompatActivity {
    //Strings to register to create intent filter for registering the recivers
    private static final String ACTION_STRING_SERVICE = "ToService";
    private static final String ACTION_STRING_ACTIVITY_SINGLE = "ToActivitySingle";
    private static final String ACTION_STRING_ACTIVITY = "ToActivity";
    private static final String ASK_FOR_DATA = "askforData";
    private static final String SEND_DATA = "sendData";
    private static final String SEND_DATA_SINGLE = "sendDataSingle";
    private HistoryViewModel historyViewModel;
    Context context;
    public HistoryActivity() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        context = this;

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.setHasFixedSize(true);

        HistoryAdapter adapter = new HistoryAdapter();
        recyclerView.setAdapter(adapter);

        historyViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(HistoryViewModel.class);

        historyViewModel.getAllHistoryMessages().observe(this, adapter::setHistories);
        //STEP2: register the receiver
        if (activityReceiver != null) {
            //Create an intent filter to listen to the broadcast sent with the action "ACTION_STRING_ACTIVITY"
            IntentFilter intentFilter = new IntentFilter(ACTION_STRING_ACTIVITY);
            //Map the intent filter to the receiver
            registerReceiver(activityReceiver, intentFilter);
            sendBroadcast();
        }
        load_Setting();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast();
        load_Setting();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //STEP3: Unregister the receiver
        unregisterReceiver(activityReceiver);
    }
    //STEP1: Create a broadcast receiver
    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String update = intent.getStringExtra(SEND_DATA);
            String updateSingle = intent.getStringExtra(SEND_DATA_SINGLE);
            Gson gsonSingle = new Gson();
            History singleHistory = gsonSingle.fromJson(updateSingle,History.class);
            if(singleHistory != null){
                historyViewModel.insert(singleHistory);
            }
            Gson gson = new Gson();
            ArrayList<History> ob = gson.fromJson(update,new TypeToken<List<History>>(){}.getType());
            if (ob != null){
                for(History hist : ob){
                    historyViewModel.insert(hist);
                }
            }
        }
    };
    //send broadcast from activity to all receivers listening to the action "ACTION_STRING_SERVICE"
    private void sendBroadcast() {
        Intent new_intent = new Intent();
        new_intent.setAction(ACTION_STRING_SERVICE);
        new_intent.putExtra(ASK_FOR_DATA,true);
        sendBroadcast(new_intent);
    }

    public void setDarkMode(boolean darkModeSetting) {
        if (darkModeSetting) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void load_Setting() {
        // Get the shared preferences
        SharedPreferences appSettingPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Set the dark mode according to the set mode
        setDarkMode(appSettingPrefs.getBoolean("darkmode", true));

        setOrientationMode(Objects.requireNonNull(appSettingPrefs.getString("Orientation", "2")));
    }

    public void setOrientationMode(String Orientation) {
        // Sets the current Orientation mode under the index of Rotation
        switch (Orientation) {
            case "1":
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                break;
            case "2":
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case "3":
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }
}