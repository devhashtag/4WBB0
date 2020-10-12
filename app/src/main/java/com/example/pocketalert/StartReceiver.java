package com.example.pocketalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class StartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (! Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            return;
        }

        Intent startIntent = new Intent(context, ForegroundService.class);
        startIntent.setAction("START");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(startIntent);
        } else {
            context.startService(startIntent);
        }
    }
}
