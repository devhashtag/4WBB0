package com.example.pocketalert;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EditDetailsActivity extends AppCompatActivity {

    private EditText name, address, email, phone, birthday;
    private TextView id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            findViews();
            setTextInView(bundle);
        } else {
            Log.e("DetailActivity", "No extras sent");
            finish();
        }
    }

    /**
     * Finds the EditTexts in this activity.
     */
    private void findViews() {
        id = findViewById(R.id.deviceID);
        name = findViewById(R.id.nameInput);
        address = findViewById(R.id.addressInput);
        phone = findViewById(R.id.phoneInput);
        email = findViewById(R.id.emailInput);
        birthday = findViewById(R.id.bdayInput);
    }

    /**
     * Sets the text in the different views to the the values provided in the extras.
     */
    private void setTextInView(@NonNull Bundle bundle) {
        id.setText(bundle.getString("id"));
        name.setText(bundle.getString("name"));
        address.setText(bundle.getString("address"));
        phone.setText(bundle.getString("phone"));
        email.setText(bundle.getString("email"));
        birthday.setText(bundle.getString("birthday"));
    }

    /**
     * When the save button is pressed, go back to the previous activity. The edited info will be sent along to be updated in the database.
     */
    public void onSaveDetails(View view) {
        Intent replyIntent = new Intent();

        putExtrasUpdatedDetails(replyIntent);
        setResult(RESULT_OK, replyIntent);

        finish();
    }

    /**
     * Puts the updated user details in the provided intent.
     *
     * @param intent The intent to add the user's details to in the extras.
     */
    private void putExtrasUpdatedDetails(@NonNull Intent intent) {
        intent.putExtra("id", id.getText().toString());
        intent.putExtra("name", name.getText().toString());
        intent.putExtra("address", address.getText().toString());
        intent.putExtra("phone", phone.getText().toString());
        intent.putExtra("email", email.getText().toString());
        intent.putExtra("birthday", birthday.getText().toString());
    }

    /**
     * When the cancel button is pressed, go back to the previous activity.
     */
    public void onCancelDetails(View view) {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }
}