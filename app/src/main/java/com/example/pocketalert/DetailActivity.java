package com.example.pocketalert;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.pocketalert.configuration.Command;
import com.example.pocketalert.connect.ConnectedActivity;
import com.example.pocketalert.connect.Message;
import com.example.pocketalert.data.Device;
import com.example.pocketalert.data.DeviceViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class DetailActivity extends ConnectedActivity implements OnMapReadyCallback {
    class Heartbeat {
        int Id;
        String DeviceId;
        double Longitude;
        // Made a type when creating the database
        double Lattitue;
        String Timestamp;
    }

    private static final int EDIT_DETAILS_ACTIVITY_REQUEST_CODE = 23;
    private static final String TAG = "DETAILACTIVITY";
    private String id, name, address, phone, email, birthday;
    private TextView idView, nameView, addressView, phoneView, emailView, birthdayView;
    private boolean wasDataUpdated = false;
    private double latitude, longitude;

    private DeviceViewModel deviceViewModel;
    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        String deviceId = getIntent().getStringExtra("id");

        device = deviceViewModel.getDevice(deviceId);

        if (device == null) {
            Log.e(TAG, "No device with given id");
            finish();
        }

        findViews();
        setTextViews();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        sendRequest(Command.Request.GET_DEVICE_LOCATION, deviceId, (Message response) -> {
            if (! Command.Response.OK.toString().equals(response.command)) {
                Log.e(TAG, "No device or heartbeat found for device");
                return;
            }

            Gson gson = new Gson();

            Heartbeat heartbeat = gson.fromJson(response.argument, Heartbeat.class);

            latitude = heartbeat.Lattitue;
            longitude = heartbeat.Longitude;

            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        });
    }

    /**
     * Assigns the extras in bundle to the instance variables.
     *
     * @param bundle The Bundle of the extras that were sent with the new Intent.
     */
//    private void assignValues(@NonNull Bundle bundle) {
//        id = bundle.getString("id");
//        name = bundle.getString("name");
//        address = bundle.getString("address");
//        phone = bundle.getString("phone");
//        email = bundle.getString("email");
//        birthday = bundle.getString("birthday");
//    }

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
        nameView.setText(device.OwnerFirstName.length() > 0 ? device.OwnerFirstName : "Name");
        addressView.setText(device.OwnerAddress.length() > 0 ? device.OwnerAddress : "Address");
        birthdayView.setText(device.DateOfBirth.length() > 0 ? device.DateOfBirth : "Birthday");
        phoneView.setText("0637684915");
        emailView.setText("john@doe.com");
    }

    /**
     * When the edit button is pressed, go to the EditDetailsActivity.
     */
    public void onEdit(View view) {
        Intent intent = new Intent(this, EditDetailsActivity.class);
        intent.putExtra("id", device.Id);
        startActivity(intent);
    }

    /**
     * In this method map functions have to be preformed.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: " + longitude + "   " + latitude);
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(name));
        // Automatically zooms in on the marker
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
    }

    @Override
    protected void onResume() {
        super.onResume();

        device = deviceViewModel.getDevice(device.Id);
        setTextViews();
    }
}