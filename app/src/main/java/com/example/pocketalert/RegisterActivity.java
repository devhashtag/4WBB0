package com.example.pocketalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.pocketalert.RegisterActivity.REPLY";

    private EditText idInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        idInput = findViewById(R.id.connectInput);
    }

    public void onConnect(View view) {
        Intent replyIntent = new Intent();
        String id = idInput.getText().toString();
        if (id.length() > 0) {
            replyIntent.putExtra(EXTRA_REPLY, id);
            setResult(RESULT_OK, replyIntent);
        } else {
            setResult(RESULT_CANCELED, replyIntent);
        }

        finish();
    }

    public void onCancelConnect(View view) {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }
}