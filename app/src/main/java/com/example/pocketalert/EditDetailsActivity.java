package com.example.pocketalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EditDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.pocketalert.EditDetailsActivity.REPLY";

    private EditText name, address, email, phone, birthday;
    private TextView id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        id = findViewById(R.id.deviceID);
        name = findViewById(R.id.nameInput);
        address = findViewById(R.id.addressInput);
        phone = findViewById(R.id.phoneInput);
        email = findViewById(R.id.emailInput);
        birthday = findViewById(R.id.bdayInput);

        Bundle bundle = getIntent().getExtras();
        String idStr = bundle.getString("idStr");
        id.setText(idStr);
    }

    public void onSaveDetails(View view) {
        Intent replyIntent = new Intent();

//        replyIntent.putExtra(EXTRA_REPLY, Integer.parseInt("0"));
        setResult(RESULT_OK, replyIntent);

        finish();
    }

    public void onCancelDetails(View view) {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }
}