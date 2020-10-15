package com.example.pocketalert.connect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.pocketalert.ForegroundService;
import com.example.pocketalert.Message;
import com.example.pocketalert.configuration.Action;

import java.util.HashMap;
import java.util.UUID;

/**
 * Singleton class
 */
public class RequestHelper extends BroadcastReceiver {
    private static final String TAG = "REQUESTHELPER";

    public interface Callback {
        void onResponse(Message message);
    }

    public interface TimeoutCallback {
        void onTimeout();
    }

    // Holds the singleton instance
    private static RequestHelper instance;

    private HashMap<String, Callback> requests = new HashMap<>();

    // Make constructor private
    private RequestHelper() { }

    /**
     * Sends request to the foreground-service
     * @param context App context
     * @param action Action to perform
     */
    public void sendRequest(Context context, Action.Control action, Callback callback) {
        sendRequest(context, action.toString(), null, callback);
    }

    /**
     * Sends request to the foreground-service
     * @param context App context
     * @param action Action to perform
     */
    public void sendRequest(Context context, Action.Request action, Callback callback) {
        sendRequest(context, action.toString(), null, callback);
    }

    /**
     * Sends request to the foreground-service
     * @param context App context
     * @param action Action to perform
     */
    public void sendRequest(Context context, Action.Request action, String argument, Callback callback) {
        sendRequest(context, action.toString(), argument, callback);
    }

    /**
     * Sends request to the foreground-service
     * @param context Context passed from activity
     * @param action The action to put as header (Action.Control or Action.Request)
     * @param argument Optional argument or data
     */
    private void sendRequest(Context context, String action, String argument, Callback callback) {
        Intent intent = new Intent(context, ForegroundService.class);
        intent.setAction(action);

        if (argument != null) {
            intent.putExtra("argument", argument);
        }

        UUID uuid = UUID.randomUUID();

        intent.putExtra("uuid", uuid.toString());

        if (callback != null) {
            requests.put(uuid.toString(), callback);
        }

        ContextCompat.startForegroundService(context, intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String prefix = "POCKETALERT.";
        // Make sure the intent comes from the service
        if (intent == null || intent.getAction() == null || ! intent.getAction().startsWith(prefix)) {
            return;
        }

        // Retrieve callback
        String messageId = intent.getStringExtra("uuid");
        Callback callback = requests.get(messageId);

        if (callback == null) {
            Log.e(TAG, "Received intent that does not belong to request: " + intent.toString());
            return;
        }

        String action = intent.getAction();
        String argument = intent.getStringExtra("argument");
        action = action.substring(prefix.length());

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
    public static synchronized RequestHelper getInstance() {
        if (instance == null) {
            instance = new RequestHelper();
        }

        return instance;
    }
}
