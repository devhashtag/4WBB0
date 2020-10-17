package com.example.pocketalert.connect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.pocketalert.configuration.Command;

import java.util.HashMap;
import java.util.UUID;

/**
 * Singleton class
 * TODO figure out how to remove requests when switching from activity
 */
public class RequestHelper extends BroadcastReceiver {
    public interface Callback {
        void onResponse(Message message);
    }

    private static final String TAG = "REQUESTHELPER";

    // Holds the singleton instance
    private static RequestHelper instance;

    // Holds the callbacks
    private HashMap<String, Callback> requests = new HashMap<>();

    // Make constructor private
    private RequestHelper() { }

//    /**
//     * Sends request to the foreground-service
//     * @param context App context
//     * @param action Action to perform
//     */
//    public void sendRequest(Context context, Command.Control action, Callback callback) {
//        sendRequest(context, action.toString(), null, callback);
//    }
//
//    /**
//     * Sends request to the foreground-service
//     * @param context App context
//     * @param action Action to perform
//     */
//    public void sendRequest(Context context, Command.Request action, Callback callback) {
//        sendRequest(context, action.toString(), null, callback);
//    }
//
//    /**
//     * Sends request to the foreground-service
//     * @param context App context
//     * @param action Action to perform
//     */
//    public void sendRequest(Context context, Command.Request action, String argument, Callback callback) {
//        sendRequest(context, action.toString(), argument, callback);
//    }

    /**
     * Sends request to the foreground-service
     * @param context Context passed from activity
     * @param action The action to put as header (Action.Control or Action.Request)
     * @param argument Optional argument or data
     */
    public void sendRequest(Context context, String scope, String action, String argument, Callback callback) {
        Intent intent = new Intent(context, ForegroundService.class);
        intent.setAction(action);

        if (argument != null) {
            intent.putExtra("argument", argument);
        }

        UUID uuid = UUID.randomUUID();
        String messageId = scope + uuid.toString();

        intent.putExtra("uuid", messageId);

        if (callback != null) {
            requests.put(messageId, callback);
        }

        ContextCompat.startForegroundService(context, intent);
    }

    /**
     * Creates a request scope
     * @return returns scopeId
     */
    public String createScope() {
        return UUID.randomUUID().toString();
    }

    /**
     * Removes all callbacks belonging to scopeId
     * @param scopeId
     */
    public void closeScope(String scopeId) {
        for (String key : requests.keySet()) {
            if (key.startsWith(scopeId)) {
                requests.remove(key);
            }
        }
    }

    // Gets called when an intent is received
    // Intents are provided by the IntentReceiver
    @Override
    public void onReceive(Context context, Intent intent) {
        // Make sure the intent comes from the service
        if (intent == null || ! Command.ACTION.equals(intent.getAction())) {
            return;
        }

        // Retrieve callback
        String action = intent.getStringExtra("action");
        String argument = intent.getStringExtra("argument");
        String messageId = intent.getStringExtra("uuid");

        Callback callback = requests.get(messageId);

        if (callback == null) {
            Log.d(TAG, "Received intent that does not belong to request: " + intent.toString());
            return;
        }

        Message message = new Message();
        message.command = action;
        message.argument = argument;

        callback.onResponse(message);

        // TODO: Figure out how to handle multiple responses on a single request
        // Like live location sharing
        requests.remove(messageId);
    }

    /**
     * Retrieve the singleton instance
     * @return Singleton RequestHelper instance
     */
    public static synchronized RequestHelper getInstance(Context context) {
        if (instance == null) {
            instance = new RequestHelper();
            context.getApplicationContext().registerReceiver(
                instance,
                new IntentFilter(Command.ACTION)
            );
        }

        return instance;
    }
}
