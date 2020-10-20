package com.example.pocketalert.connect;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.*;

import com.example.pocketalert.DetailActivity;
import com.example.pocketalert.MainActivity;
import com.example.pocketalert.R;
import com.example.pocketalert.configuration.*;

public class ForegroundService extends Service {
    private static final String TAG = "ForegroundService";
    private static final String CHANNEL_ID = "PocketAlertNotificationChannel";

    private WakeLock wakeLock = null;
    private boolean isServiceStarted = false;
    private Executor executor = Executors.newSingleThreadExecutor();

    // Used for passing the intents from the activity to the communication thread
    private Queue<Intent> intentQueue = new ConcurrentLinkedQueue<Intent>();

    public ForegroundService() { }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: startId=" + startId);
        String action = intent.getAction();

        // Short circuiting
        if (! Command.contains(action) ||
                (Command.isResponse(action) &&
                !Command.isRequest(action) &&
                !Command.isControl(action))) {
            Log.e(TAG, "Action provided was not in the Action class or is a response: " + action);
            return START_STICKY;
        }

        // Forwarding requests
        if (Command.isRequest(action)) {
            intentQueue.add(intent);
            return START_STICKY;
        }

        // Only start/stop are meaningful in this context
        Command.Control controlAction  = Command.Control.valueOf(action);

        switch (controlAction) {
            case START_SERVICE:
                startService();
                break;
            case STOP_SERVICE:
                stopService();
                break;
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Notification notification = createNotification();
        startForeground(1, notification);
        Log.d(TAG, "The service has been created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopService();
        Log.d(TAG, "The service has been destroyed");
    }

    @SuppressLint("WakelockTimeout")
    private void startService() {
        if (isServiceStarted) {
            return;
        }

        isServiceStarted = true;

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PocketAlert::wakeLock");
        wakeLock.acquire();

        executor.execute(this::run);
    }

    private void stopService() {
        Log.d(TAG, "Stopping the service task");

        try {
            if (wakeLock.isHeld()) {
                wakeLock.release();
            }

            stopForeground(true);
            stopSelf();
        } catch (Exception exception) {
            Log.d(TAG, "Exception occurred when shutting down service: " + exception.getMessage());
        }

        isServiceStarted = false;
    }

    private void setNotification(Notification notification) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    // Creates a channel if required
    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Someone needs your help",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("Server communication");
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[] {100, 200, 300, 400, 500});

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @NonNull
    private Notification createNotification() {
        createChannel();

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Notification.Builder builder = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? new Notification.Builder(this, CHANNEL_ID)
                : new Notification.Builder(this);

        return builder
                .setContentTitle("Endless service")
                .setContentText("This belongs to the PocketAlert app")
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_noti_icon)
                .build();
    }

    @NonNull
    // Notification to show when an alert is active
    private Notification createAlertNotification(String deviceId) {
        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                new Intent(getApplicationContext(), DetailActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Notification.Builder builder = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? new Notification.Builder(this, CHANNEL_ID)
                : new Notification.Builder(this);

        Notification notification = builder
                .setContentTitle("ALERT")
                .setContentText("Stap in, oma is gevallen")
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_noti_icon)
                .build();

        return notification;
    }

    /**
     *
     * Handles communication with the Server.
     * Should NOT be run the UI thread (it is threadsafe)
     */
    private void run() {
        ConnectionHandler handler = new ConnectionHandler((@NotNull Throwable t, @Nullable Response response) -> {
            Log.e(TAG, "Error in websocket connection: " + t.getMessage() + response);

            // Notify app of error
            Intent intent = new Intent(Command.ACTION);
            intent.putExtra("action", Command.Control.CONNECTION_ERROR.toString());
            intent.putExtra("error", t.toString());

            if (response != null) {
                intent.putExtra("response", response.toString());
            }

            sendBroadcast(intent);
        },
        (@NotNull WebSocket webSocket, @NotNull Queue<Message> messageQueue) -> {
            // Handle intents
            while (! intentQueue.isEmpty()) {
                Intent intent = intentQueue.poll();
                String action = intent.getAction();
                String messageId = intent.getStringExtra("uuid");

                // Directly create message if the action is in the Response enum
                if (Command.isRequest(action)) {
                    Message message =
                            messageId == null
                            ? new Message()
                            : new Message(messageId);

                    message.command = action;
                    message.argument = intent.getStringExtra("argument");

                    webSocket.send(message.toString());
                } else {
                    Log.e(TAG, "Received intent that was not a request: " + action);
                }
            }

            // Handle messages
            while (! messageQueue.isEmpty()) {
                Message message = messageQueue.poll();

                // Start alert
                if (Command.Response.ALERT.toString().equals(message.command)) {
                    // TODO: send extras
                    setNotification(
                        createAlertNotification(message.argument)
                    );
                    Log.d(TAG, "Received alert");
                    continue;
                }

                if (Command.Response.CANCEL_ALERT.toString().equals(message.command)) {
                    setNotification(
                            createNotification()
                    );
                    Log.d(TAG, "Received cancel alert");
//                    Intent intent = new Intent(this, DetailActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                    continue;
                }

                Intent intent = new Intent(Command.ACTION);
                intent.putExtra("action", message.command);
                intent.putExtra("argument", message.argument);
                intent.putExtra("uuid", message.getMessageId());

                sendBroadcast(intent);
            }

            return isServiceStarted;
        });

        handler.run();
    }

    /**
     *
     * Helper classes and interfaces for the abstraction from OkHttp
     */
    private class ConnectionHandler {
        private ErrorHandler errorHandler;
        private CommunicationLoop loop;

        private Queue<Message> messageQueue = new ConcurrentLinkedQueue<Message>();

        public ConnectionHandler(ErrorHandler errorHandler, CommunicationLoop loop) {
            this.errorHandler = errorHandler;
            this.loop = loop;
        }

        public void run() {
            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("ws://192.168.2.55:5007/websocket")
                    .build();

            MessageHandler handler = (@NotNull String text) -> {
                Message message = Message.fromString(text);
                messageQueue.add(message);
            };

            WebSocketListener listener = new WebSocketClient(handler, errorHandler);
            WebSocket webSocket = httpClient.newWebSocket(request, listener);
            httpClient.dispatcher().executorService().shutdown();

            boolean isRunning = true;

            while (isRunning) {
                // Execute function
                isRunning = loop.loop(webSocket, messageQueue);

                // Prevent 100% CPU-usage (runs < 20 times a second)
                try {
                    Thread.sleep(50);
                } catch (InterruptedException exception) {
                    Log.e(TAG, "work interrupted");
                }
            }

            // Clean up
            webSocket.close(1001,"Service stopped" );
            Log.d(TAG, "Work finished (stopped)");
        }
    }

    private class WebSocketClient extends WebSocketListener {
        private MessageHandler messageHandler;
        private ErrorHandler errorHandler;

        public WebSocketClient(MessageHandler messageHandler, ErrorHandler errorHandler) {
            this.messageHandler = messageHandler;
            this.errorHandler = errorHandler;
        }

        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosed(webSocket, code, reason);
            Log.d(TAG, "Closed connection (with code " + code + ") : " + reason);
        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);

            errorHandler.onError(t, response);
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            super.onMessage(webSocket, text);

            messageHandler.onMessage(text);
        }

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);
            Log.d(TAG, "Opened connection!");
        }
    }

    private interface MessageHandler {
        void onMessage(@NotNull String text);
    }

    private interface ErrorHandler {
        void onError(@NotNull Throwable t, @Nullable Response response);
    }

    private interface CommunicationLoop {
        boolean loop(@NotNull WebSocket webSocket, @NotNull Queue<Message> messageQueue);
    }
}
