package com.example.pocketalert;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;
import java.util.Random;

import static com.example.pocketalert.MainActivity.EDIT_DETAILS_ACTIVITY_REQUEST_CODE;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String id, name, address, phone, email, birthday;
    private TextView idView, nameView, addressView, phoneView, emailView, birthdayView;
    private boolean wasDataUpdated = false;
    private double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            assignValues(bundle);
        } else {
            Log.e("DetailActivity", "No extras sent");
            finish();
        }

        findViews();
        setTextViews();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // just so the location is different each time. replace with location of device.
        Random random = new Random();
        latitude = 51.844975 + random.nextDouble() - 0.5;
        longitude = 4.927505 + random.nextDouble() - 0.5;
    }

    /**
     * Assigns the extras in bundle to the instance variables.
     *
     * @param bundle The Bundle of the extras that were sent with the new Intent.
     */
    private void assignValues(@NonNull Bundle bundle) {
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
     * When another Activity returns a result, this is where that result gets processed.
     *
     * @param requestCode The request code that was used when the activity was started.
     * @param resultCode  The result code return by the activity.
     * @param data        The Intent data the activity return.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_DETAILS_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                if (wasUpdated(extras)) {
                    assignValues(extras);
                    setTextViews();
                    wasDataUpdated = true;
                    sendReply();
                }
            }
        }
    }

    /**
     * Checks if the values have been updated in the edit activity. If not, this can be noted to prevent the app to run unnecessary code later on.
     *
     * @param bundle A bundle of extras.
     * @return false if nothing has changed, true if one of the fields has been updated.
     */
    private boolean wasUpdated(@NonNull Bundle bundle) {
        return !(Objects.equals(bundle.getString("name"), name) &&
                Objects.equals(bundle.getString("address"), address) &&
                Objects.equals(bundle.getString("phone"), phone) &&
                Objects.equals(bundle.getString("email"), email) &&
                Objects.equals(bundle.getString("birthday"), birthday));
    }

    /**
     * When the edit button is pressed, go to the EditDetailsActivity.
     */
    public void onEdit(View view) {
        Intent intent = new Intent(this, EditDetailsActivity.class);
        putExtrasDetails(intent);
        startActivityForResult(intent, EDIT_DETAILS_ACTIVITY_REQUEST_CODE);
    }

    /**
     * Puts the updated user details in the provided intent.
     *
     * @param intent The intent to add the user's details to in the extras.
     */
    private void putExtrasDetails(@NonNull Intent intent) {
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("address", address);
        intent.putExtra("phone", phone);
        intent.putExtra("email", email);
        intent.putExtra("birthday", birthday);
    }

    private void sendReply() {
        Intent replyIntent = new Intent();

        if (wasDataUpdated) {
            putExtrasDetails(replyIntent);
            setResult(RESULT_OK, replyIntent);
        } else {
            setResult(RESULT_CANCELED, replyIntent);
        }

        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(name));
    }
}