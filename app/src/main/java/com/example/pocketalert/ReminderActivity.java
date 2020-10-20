package com.example.pocketalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.pocketalert.configuration.Command;
import com.example.pocketalert.connect.ConnectedActivity;
import com.example.pocketalert.connect.Message;
import com.example.pocketalert.database.User;
import com.example.pocketalert.database.UserViewModel;
import com.example.pocketalert.databaseHistory.HistoryViewModel;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ReminderActivity extends ConnectedActivity implements  TimePickerDialog.OnTimeSetListener{

    private int currentYear = 0;
    private int currentMonth = 0;
    private int currentDay = 0;
    private int tempIndex = -1;
    private LinearLayout linearLayout;
    private UserViewModel userViewModel;

    final ArrayList<String> notificationID = new ArrayList<>();
    final ArrayList<String> calendarStrings = new ArrayList<>();
    final ArrayList<Integer> currentDayArray = new ArrayList<>();
    final ArrayList<Integer> currentMonthArray = new ArrayList<>();
    final ArrayList<Integer> currentYearArray = new ArrayList<>();
    final ArrayList<Integer> currentHourArray = new ArrayList<>();
    final ArrayList<Integer> currentMinuteArray = new ArrayList<>();
    final ArrayList<TextView> currentTextViewArray = new ArrayList<>();
    final ArrayList<Event> eventArray = new ArrayList<>();
    final ArrayList<Button> buttonArray = new ArrayList<>();
    final ArrayList<LinearLayout> linearLayouts = new ArrayList<>();
    final ArrayList<reminder> reminder = new ArrayList<>();

    List<String> listDropdown = new ArrayList<String>();

    private Spinner spinner;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        final Button currentDateButton = findViewById(R.id.currentDayButton);
        final Button pickTime = findViewById(R.id.ButtonSetTime);
        final LinearLayout linearLayout = findViewById(R.id.dayContent);
        final Spinner spinner = findViewById(R.id.dropdownID);
        userViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(UserViewModel.class);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,userViewModel.getAllUsersID());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        if(userViewModel.getAllUsersID().size() > 0){
            findViewById(R.id.ll).setVisibility(View.VISIBLE);
            findViewById(R.id.textView).setVisibility(View.INVISIBLE);
            getInfo();
        } else {
            findViewById(R.id.ll).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView).setVisibility(View.VISIBLE);
        }

        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);
        currentDay = c.get(Calendar.DATE);

        final CompactCalendarView compactCalendarView = findViewById(R.id.calendarView);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary

        final ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setTitle(Calendar.getInstance().get(Calendar.MONTH)+1 + " - " +  Calendar.getInstance().get(Calendar.YEAR));

        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateClicked);

                currentYear = cal.get(Calendar.YEAR);
                currentMonth = cal.get(Calendar.MONTH);
                currentDay = cal.get(Calendar.DATE);
                printHours();
                if(linearLayout.getVisibility() == View.GONE){
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                for(TextView t : currentTextViewArray ){
                    linearLayout.removeView(t);
                }
                for(Button b : buttonArray){
                    linearLayout.removeView(b);
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(firstDayOfNewMonth);
                cal.get(Calendar.YEAR);
                currentYear = cal.get(Calendar.YEAR);
                currentMonth = cal.get(Calendar.MONTH);
                currentDay = cal.get(Calendar.DATE);
                actionbar.setTitle((currentMonth+1)  +" - " + (currentYear));

                for(int i =0; i<currentDayArray.size();i++){
                    if(currentYearArray.get(i)==currentYear && currentDayArray.get(i)==currentDay && currentMonthArray.get(i)==currentMonth){
                        linearLayout.addView(buttonArray.get(i));
                        linearLayout.addView(currentTextViewArray.get(i));
                        return;
                    }
                }
                if(linearLayout.getVisibility() == View.GONE){
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        // The button save into the list
        pickTime.setOnClickListener(v -> {
            DialogFragment timePicker = new TimePickerFragment();
            actionbar.setTitle(Calendar.getInstance().get(Calendar.MONTH)+1 + " - " + Calendar.getInstance().get(Calendar.YEAR));
            timePicker.show(getSupportFragmentManager(),"time picker");
        });
        currentDateButton.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            compactCalendarView.setCurrentDate(cal.getTime());
        });

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

    @Override
    protected void onResume() {
        super.onResume();
        load_Setting();
        //getInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //saveInfo();
    }

    private void saveInfo(){
        File file = new File(this.getFilesDir(),"saved");
        File minutesFile = new File(this.getFilesDir(),"minutes");
        File hoursFile = new File(this.getFilesDir(),"hours");
        File daysFile = new File(this.getFilesDir(),"days");
        File monthsFile = new File(this.getFilesDir(),"months");
        File yearsFile = new File(this.getFilesDir(),"years");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOut));

            FileOutputStream fOutMinutes = new FileOutputStream(minutesFile);
            BufferedWriter bwMinutes = new BufferedWriter(new OutputStreamWriter(fOutMinutes));

            FileOutputStream fOutHours = new FileOutputStream(hoursFile);
            BufferedWriter bwHours = new BufferedWriter(new OutputStreamWriter(fOutHours));

            FileOutputStream fOutDays = new FileOutputStream(daysFile);
            BufferedWriter bwDays = new BufferedWriter(new OutputStreamWriter(fOutDays));

            FileOutputStream fOutMonths = new FileOutputStream(monthsFile);
            BufferedWriter bwMonths = new BufferedWriter(new OutputStreamWriter(fOutMonths));

            FileOutputStream fOutYears = new FileOutputStream(yearsFile);
            BufferedWriter bwYears = new BufferedWriter(new OutputStreamWriter(fOutYears));

            for  (int i = 0 ; i < currentDayArray.size() ; i++){
                bw.write(calendarStrings.get(i));
                bw.newLine();
                bw.write(notificationID.get(i));
                bw.newLine();
                bwMinutes.write(currentMinuteArray.get(i));
                bwHours.write(currentHourArray.get(i));
                bwDays.write(currentDayArray.get(i));
                bwMonths.write(currentMonthArray.get(i));
                bwYears.write(currentYearArray.get(i));
            }
            bw.close();
            fOut.close();
            bwDays.close();
            fOutDays.close();
            bwMonths.close();
            fOutMonths.close();
            bwYears.close();
            fOutYears.close();
            bwHours.close();
            fOutHours.close();
            bwMinutes.close();
            fOutMinutes.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getInfo(){
        File file = new File(this.getFilesDir(),"saved");
        File minutesFile = new File(this.getFilesDir(),"minutes");
        File hoursFile = new File(this.getFilesDir(),"hours");
        File daysFile = new File(this.getFilesDir(),"days");
        File monthsFile = new File(this.getFilesDir(),"months");
        File yearsFile = new File(this.getFilesDir(),"years");

        if(!file.exists()){
            return;
        }
        try{

            FileInputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            FileInputStream isMinutes = new FileInputStream(minutesFile);
            BufferedReader readerMinutes= new BufferedReader(new InputStreamReader(isMinutes));

            FileInputStream isHours = new FileInputStream(hoursFile);
            BufferedReader readerHours = new BufferedReader(new InputStreamReader(isHours));

            FileInputStream isDays = new FileInputStream(daysFile);
            BufferedReader readerDays = new BufferedReader(new InputStreamReader(isDays));

            FileInputStream isMonths = new FileInputStream(monthsFile);
            BufferedReader readerMonths = new BufferedReader(new InputStreamReader(isMonths));

            FileInputStream isYears = new FileInputStream(yearsFile);
            BufferedReader readerYears = new BufferedReader(new InputStreamReader(isYears));


            String line = reader.readLine();
            while(line!=null){
                calendarStrings.add(line);
                line = reader.readLine();
                notificationID.add(line);
                line = reader.readLine();
                currentMinuteArray.add(readerMinutes.read());
                currentHourArray.add(readerHours.read());
                currentDayArray.add(readerDays.read());
                currentMonthArray.add(readerMonths.read());
                currentYearArray.add(readerYears.read());
            }
            is.close();
            isMinutes.close();
            isHours.close();
            isDays.close();
            isMonths.close();
            isYears.close();
            reader.close();
            readerMinutes.close();
            readerHours.close();
            readerDays.close();
            readerMonths.close();
            readerYears.close();
        } catch ( Exception e) {
            e.printStackTrace();
        }
        for(int i = 0; i < currentMinuteArray.size(); i++){

            CompactCalendarView compactCalendarView = findViewById(R.id.calendarView);
            linearLayout = findViewById(R.id.dayContent);
            String s;
            s = calendarStrings.get(i);
            // Add a linear layout which is horizontal
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            LinearLayout tempLinearLayout = new LinearLayout(getApplicationContext());
            params2.width=275;
            tempLinearLayout.setLayoutParams(params);
            tempLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Add the delete button
            Button newButton = new Button(this);
            newButton.setTextSize(30);
            newButton.setTextColor(getColor(R.color.mainText));
            newButton.setLayoutParams(params2);
            newButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove, 0, 0, 0);
            newButton.setOnClickListener(v -> {
                for(int i1 = 0; i1 < currentYearArray.size(); i1++){
                    if(buttonArray.get(i1) == v){
                        tempIndex = i1;
                    }
                }
                if(tempIndex > -1){
                    linearLayouts.get(tempIndex).removeView(v);
                    linearLayouts.get(tempIndex).removeView(currentTextViewArray.get(tempIndex));
                    linearLayout.removeView(linearLayouts.get(tempIndex));
                    compactCalendarView.removeEvent(eventArray.get(tempIndex),true);
                    calendarStrings.remove(tempIndex);
                    currentDayArray.remove(tempIndex);
                    currentMonthArray.remove(tempIndex);
                    currentYearArray.remove(tempIndex);
                    currentHourArray.remove(tempIndex);
                    currentMinuteArray.remove(tempIndex);
                    currentTextViewArray.remove(tempIndex);
                    // Send remove to server
                    notificationID.remove(tempIndex);
                    buttonArray.remove(tempIndex);
                    eventArray.remove(tempIndex);
                    linearLayouts.remove(tempIndex);
                    reminder.remove(tempIndex);
                }
            });

            buttonArray.add(newButton);
            tempLinearLayout.addView(newButton);
            // Add the textview
            TextView newTextView = new TextView(this);
            newTextView.setText(s);
            newTextView.setTextSize(30);
            newTextView.setTextColor(getColor(R.color.mainText));
            newTextView.setLayoutParams(params);
            currentTextViewArray.add(newTextView);
            tempLinearLayout.addView(newTextView);

            Calendar c = Calendar.getInstance();
            c.set(currentYearArray.get(i),currentMonthArray.get(i),currentDayArray.get(i));
            // Set the day to green
            Event ev2 = new Event(Color.GREEN, c.getTimeInMillis());
            compactCalendarView.addEvent(ev2,true);
            eventArray.add(ev2);
            // Increase the index
            linearLayouts.add(tempLinearLayout);
            c.set(currentYearArray.get(i),currentMonthArray.get(i),currentDayArray.get(i),currentHourArray.get(i),currentMinuteArray.get(i));
            spinner = findViewById(R.id.dropdownID);

            String userID = spinner.getSelectedItem().toString();
            reminder r = new reminder(userID,c.getTimeInMillis());
            reminder.add(r);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        addTextView(hourOfDay,minute);
    }

    public static class TimePickerFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hour,minute, DateFormat.is24HourFormat(getActivity()));
        }
    }
    void addTextView(int hourOfDay, int minute){
        CompactCalendarView compactCalendarView = findViewById(R.id.calendarView);
        EditText editText = findViewById(R.id.extraInfoPickTime);
        linearLayout = findViewById(R.id.dayContent);
        String s;
        String tempStringHour = String.valueOf(hourOfDay);
        String tempStringMinute = String.valueOf(minute) ;

        if((hourOfDay %10)==hourOfDay){
            tempStringHour = "0"+tempStringHour;
        }
        if((minute %10)==minute){
            tempStringMinute = "0"+tempStringMinute;
        }
        if(editText.getText().toString().equals("")){
            s = tempStringHour + ":" + tempStringMinute;
        } else {
            s = tempStringHour + ":" + tempStringMinute + "  -  " +editText.getText();
        }
        calendarStrings.add(s);
        currentDayArray.add(currentDay);
        currentMonthArray.add(currentMonth);
        currentYearArray.add(currentYear);
        currentHourArray.add(hourOfDay);
        currentMinuteArray.add(minute);

        // Add a linear layout which is horizontal
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        LinearLayout tempLinearLayout = new LinearLayout(getApplicationContext());
        params2.width=275;
        tempLinearLayout.setLayoutParams(params);
        tempLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Add the delete button
        Button newButton = new Button(this);
        newButton.setTextSize(30);
        newButton.setTextColor(getColor(R.color.mainText));
        newButton.setLayoutParams(params2);
        newButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove, 0, 0, 0);
        newButton.setOnClickListener(v -> {
            for(int i = 0; i < currentYearArray.size();i++){
                if(buttonArray.get(i) == v){
                    tempIndex = i;
                }
            }
            if(tempIndex > -1){
                linearLayouts.get(tempIndex).removeView(v);
                linearLayouts.get(tempIndex).removeView(currentTextViewArray.get(tempIndex));
                linearLayout.removeView(linearLayouts.get(tempIndex));
                compactCalendarView.removeEvent(eventArray.get(tempIndex),true);
                calendarStrings.remove(tempIndex);
                currentDayArray.remove(tempIndex);
                currentMonthArray.remove(tempIndex);
                currentYearArray.remove(tempIndex);
                currentHourArray.remove(tempIndex);
                currentMinuteArray.remove(tempIndex);
                currentTextViewArray.remove(tempIndex);

                sendRequest(Command.Request.REMOVE_MEDICINE_REMINDER, reminder.get(tempIndex).getDeviceID(), null);
                reminder.remove(tempIndex);
                // Send remove to server
                notificationID.remove(tempIndex);
                buttonArray.remove(tempIndex);
                eventArray.remove(tempIndex);
                linearLayouts.remove(tempIndex);
            }
        });
        Calendar calID = Calendar.getInstance();
        calID.set(currentYear,currentMonth,currentDay,hourOfDay,minute);
        spinner = findViewById(R.id.dropdownID);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //String userID = spinner.getSelectedItem().toString();
        String userID = preferences.getString("userID", null);
        userID = "9ebc2806-45e0-475d-908f-e7346cb13426";
        reminder r = new reminder(userID,calID.getTimeInMillis());
        Gson gson = new Gson();
        String jsonObject = gson.toJson(r);
        reminder.add(r);

        sendRequest(Command.Request.SEND_MEDICINE_REMINDER, jsonObject, (Message response) -> {
            notificationID.add(response.argument);
        });
        buttonArray.add(newButton);
        tempLinearLayout.addView(newButton);
        // Add the textview
        TextView newTextView = new TextView(this);
        newTextView.setText(s);
        newTextView.setTextSize(30);
        newTextView.setTextColor(getColor(R.color.mainText));
        newTextView.setLayoutParams(params);
        currentTextViewArray.add(newTextView);
        tempLinearLayout.addView(newTextView);

        Calendar c = Calendar.getInstance();
        c.set(currentYear,currentMonth,currentDay);

        // Set the day to green
        Event ev2 = new Event(Color.GREEN, c.getTimeInMillis());
        compactCalendarView.addEvent(ev2,true);
        eventArray.add(ev2);
        // Increase the index
        linearLayouts.add(tempLinearLayout);
        printHours();
    }
    void printHours(){
        ArrayList<sortObject> tvArray= new ArrayList<>();


        for(LinearLayout l : linearLayouts){
            linearLayout.removeView(l);
        }
        final ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setTitle((currentMonth+1)  +" - " + (currentYear));

        for(int i =0; i<currentDayArray.size();i++){
            if(currentYearArray.get(i)==currentYear && currentDayArray.get(i)==currentDay && currentMonthArray.get(i)==currentMonth){
                tvArray.add(new sortObject(i, currentHourArray.get(i), currentMinuteArray.get(i)));
            }
        }

        Collections.sort(tvArray);
        for(sortObject i : tvArray){
            linearLayout.addView(linearLayouts.get(i.thisIndex));
        }
    }
    public static class sortObject implements Comparable<sortObject>{
        int thisIndex;
        int thisHour;
        int thisMinute;
        public sortObject(int indexx, int hourr, int minutee){
            this.thisIndex = indexx;
            this.thisHour = hourr;
            this.thisMinute = minutee;
        }

        public int getThisHour() {
            return thisHour;
        }

        public int getThisMinute() {
            return thisMinute;
        }

        @Override
        public int compareTo(sortObject o) {
            int otherHour =  o.getThisHour();
            int otherMinute = o.getThisMinute();
            if(thisHour - otherHour != 0){
                return thisHour - otherHour;
            } else {
                return thisMinute - otherMinute;
            }
        }

        @NonNull
        @Override
        public String toString() {
            return "sortObject{" +
                    "thisIndex=" + thisIndex +
                    ", thisHour=" + thisHour +
                    ", thisMinute=" + thisMinute +
                    '}';
        }
    }
    public static class reminder{
        public String deviceId;
        public long timestamp;
        public reminder(String devID, Long milliesconds){
            this.deviceId = devID;
            this.timestamp = milliesconds;
        }

        public String getDeviceID() {
            return deviceId;
        }
    }
}