package com.example.pocketalert.connect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pocketalert.R;
import com.example.pocketalert.RegisterInstructionsActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText idInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        idInput = findViewById(R.id.connectInput);
    }

    /**
     * When the connect button is pressed, go back to the previous activity. If an ID was entered, that gets sent back as an extra to be added to the database.
     */
    public void onConnect(View view) {
        Intent replyIntent = new Intent();
        String id = idInput.getText().toString();
        if (id.length() > 0) {
            replyIntent.putExtra("id", id);
            setResult(RESULT_OK, replyIntent);
        } else {
            setResult(RESULT_CANCELED, replyIntent);
        }

        finish();
    }

    /**
     * When the cancel button is pressed, go back to the previous activity.
     */
    public void onCancelConnect(View view) {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }

    /**
     * When the how to connect to a new device button is pressed, go to the register instructions activity.
     */
    public void howToConnect(View view) {
        Intent intent = new Intent(this, RegisterInstructionsActivity.class);
        startActivity(intent);
    }
}