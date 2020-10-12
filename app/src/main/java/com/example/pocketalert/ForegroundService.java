package com.example.pocketalert;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;

import kotlin.reflect.KFunction;
import okhttp3.*;

public class ForegroundService extends Service {
    private static final String TAG = "ForegroundService";
    private static final String CHANNEL_ID = "PocketAlertNotificationChannel";

    private WakeLock wakeLock = null;
    private boolean isServiceStarted = false;
    private Executor executor = Executors.newSingleThreadExecutor();

    public ForegroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: startId=" + startId);

        if (intent != null) {
            String action = intent.getAction();

            switch (action) {
                case "START":
                    startService();
                    break;
                case "STOP":
                    stopService();
                    break;
                default:
                    Log.d(TAG, "onStartCommand: This should never happen. Received intent with action=" + action);
                    break;
            }
        } else {
            Log.d(TAG, "onStartCommand: Received null intent");
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "The service has been created");

        Notification notification = createNotification();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopService();

        Log.d(TAG, "The service has been destroyed");

        Toast.makeText(this, "PocketAlert service destroyed", Toast.LENGTH_SHORT);
    }

    private void startService() {
        if (isServiceStarted) {
            return;
        }

        Toast.makeText(this, "Starting the service task", Toast.LENGTH_SHORT);
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

    private Notification createNotification() {
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
                .setWhen(System.currentTimeMillis())
                .build();
    }

    private void run() {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("ws://192.168.2.11:50007/")
                .build();

        WebSocketListener listener = new WebSocketClient(
            (@NotNull String text) -> {
                Log.e(TAG, "Message received: " + text) ;
            },
            (@NotNull Throwable t, @NotNull Response response) -> {
                Log.e(TAG, "Error in websocket connection: ", t) ;
            });
        WebSocket webSocket = httpClient.newWebSocket(request, listener);

        webSocket.send("Ping");
        httpClient.dispatcher().executorService().shutdown();

        while (isServiceStarted) {
            try {
                Log.d(TAG, "Service working");
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                Log.d(TAG, "work interrupted");
            }
        }

        webSocket.close(1001,"Service stopped" );
        Log.d(TAG, "Work finished (stopped)");
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
        void onError(@NotNull Throwable t, @NotNull Response repsonse);
    }
}
