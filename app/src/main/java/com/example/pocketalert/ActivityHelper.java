package com.example.pocketalert;

import android.app.Activity;
import android.content.pm.ActivityInfo;

public class ActivityHelper {
    public static int OrientationMode = 2;
    public static void initialize(Activity activity) {
        //Do all sorts of common task for your activities here including:
        if(OrientationMode==1) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
        else if(OrientationMode==2){
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else if(OrientationMode==3){
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}

