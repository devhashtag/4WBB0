package com.example.pocketalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.pocketalert.MainActivity.EDIT_DETAILS_ACTIVITY_REQUEST_CODE;

public class DetailActivity extends AppCompatActivity {

    private String id, name, address, phone, email, birthday;
    private TextView idView, nameView, addressView, phoneView, emailView, birthdayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            assignValues(b);
        } else {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            finish();
        }

        findViews();
        setTextViews();
    }

    /**
     * Assigns the extras in bundle to the instance variables.
     *
     * @param bundle The Bundle of the extras that were sent with the new Intent.
     */
    private void assignValues(Bundle bundle) {
        id = bundle.getString("id");
        name = bundle.getString("name");
        address = bundle.getString("address");
        phone = bundle.getString("phone");
        email = bundle.getString("email");
        birthday = bundle.getString("birthday");
    }

    /**
     * Finds the TextViews in this activity.
     */
    private void findViews() {
        idView = findViewById(R.id.idDetail);
        nameView = findViewById(R.id.nameDetail);
        addressView = findViewById(R.id.addressDetail);
        phoneView = findViewById(R.id.phoneDetail);
        emailView = findViewById(R.id.emailDetail);
        birthdayView = findViewById(R.id.bdayDetail);
    }

    /**
     * Sets the TextViews to the the value stored in the instance variables. Unless the instance variable is "", then a place holder String is assigned.
     */
    private void setTextViews() {
        idView.setText(id);
        nameView.setText(name.length() > 0 ? name : "Name");
        addressView.setText(address.length() > 0 ? address : "Address");
        phoneView.setText(phone.length() > 0 ? phone : "Phone");
        emailView.setText(email.length() > 0 ? email : "Email");
        birthdayView.setText(birthday.length() > 0 ? birthday : "Birthday");
    }

    /**
     * When the edit button is pressed, go to the EditDetailsActivity.
     */
    public void onEdit(View view) {
        Intent intent = new Intent(this, EditDetailsActivity.class);
        startActivityForResult(intent, EDIT_DETAILS_ACTIVITY_REQUEST_CODE);
    }
}