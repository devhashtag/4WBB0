package com.example.pocketalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    public static final String REGISTER_ACTIVITY_REPLY = "com.pocketalert.RegisterActivity.REPLY";

    private EditText idInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        idInput = findViewById(R.id.connectInput);
    }

    /**
     * When the connect button is pressed, go back to the main activity. If an ID was entered, that gets sent back as an extra to be added to the database.
     */
    public void onConnect(View view) {
        Intent replyIntent = new Intent();
        String id = idInput.getText().toString();
        if (id.length() > 0) {
            replyIntent.putExtra(REGISTER_ACTIVITY_REPLY, id);
            setResult(RESULT_OK, replyIntent);
        } else {
            setResult(RESULT_CANCELED, replyIntent);
        }

        finish();
    }

    /**
     * When the cancel button is pressed, go back to the main activity.
     */
    public void onCancelConnect(View view) {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }
}