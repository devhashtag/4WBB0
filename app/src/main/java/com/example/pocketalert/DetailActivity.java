package com.example.pocketalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.pocketalert.MainActivity.EDIT_DETAILS_ACTIVITY_REQUEST_CODE;

public class DetailActivity extends AppCompatActivity {

    String[] userData;
    private String id, name, address, phone, email, birthday;
    private TextView idView, nameView, addressView, phoneView, emailView, birthdayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            userData = b.getStringArray("userData");
        } else {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            finish();
        }

        assignValues();
        findViews();
        setTextViews();
    }

    private void assignValues() {
        id = userData[0];
        name = userData[1];
        address = userData[2];
        phone = userData[3];
        email = userData[4];
        birthday = userData[5];
    }

    private void findViews() {
        idView = findViewById(R.id.idDetail);
        nameView = findViewById(R.id.nameDetail);
        addressView = findViewById(R.id.addressDetail);
        phoneView = findViewById(R.id.phoneDetail);
        emailView = findViewById(R.id.emailDetail);
        birthdayView = findViewById(R.id.bdayDetail);
    }

    private void setTextViews() {
        idView.setText(id);
        nameView.setText(name.length() > 0 ? name : "Name");
        addressView.setText(address.length() > 0 ? name : "Address");
        phoneView.setText(phone.length() > 0 ? name : "Phone");
        emailView.setText(email.length() > 0 ? name : "Email");
        birthdayView.setText(birthday.length() > 0 ? name : "Birthday");
    }

    public void onEdit(View view) {
        Intent intent = new Intent(this, EditDetailsActivity.class);
        startActivityForResult(intent, EDIT_DETAILS_ACTIVITY_REQUEST_CODE);
    }
}