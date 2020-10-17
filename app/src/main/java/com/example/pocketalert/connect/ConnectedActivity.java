package com.example.pocketalert.connect;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pocketalert.configuration.Command;

public abstract class ConnectedActivity extends AppCompatActivity {
    private RequestHelper requestHelper;
    private String scopeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestHelper = RequestHelper.getInstance(this);
        scopeId = requestHelper.createScope();
    }

    protected void sendRequest(Command.Request action, String argument, RequestHelper.Callback callback) {
        sendRequest(action.toString(), argument, callback);
    }

    protected void sendRequest(Command.Control action, String argument, RequestHelper.Callback callback) {
        sendRequest(action.toString(), argument, callback);
    }

    private void sendRequest(String action, String argument, RequestHelper.Callback callback) {
        requestHelper.sendRequest(this, scopeId, action, argument, callback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestHelper.closeScope(scopeId);
    }
}
