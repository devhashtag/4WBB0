package com.example.pocketalert;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.pocketalert.configuration.Command;
import com.example.pocketalert.connect.ConnectedActivity;
import com.example.pocketalert.connect.Message;
import com.example.pocketalert.data.Device;
import com.example.pocketalert.data.DeviceViewModel;
import com.google.gson.Gson;

public class EditDetailsActivity extends ConnectedActivity {

    private EditText name, address, email, phone, birthday;
    private TextView id;

    private DeviceViewModel deviceViewModel;
    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        String deviceId = getIntent().getStringExtra("id");

        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        device = deviceViewModel.getDevice(deviceId);

        if (device == null) {
            finish();
        }

        findViews();
        setTextInView();

        // Can't change the id
        id.setEnabled(false);
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
    private void setTextInView() {
        id.setText(device.Id);
        name.setText(device.OwnerFirstName);
        address.setText(device.OwnerAddress);
//        phone.setText(bundle.getString("phone"));
//        email.setText(bundle.getString("email"));
        birthday.setText(device.DateOfBirth);
    }

    /**
     * When the save button is pressed, go back to the previous activity. The edited info will be sent along to be updated in the database.
     */
    public void onSaveDetails(View view) {
        device.OwnerFirstName = name.getText().toString();
        device.DateOfBirth = birthday.getText().toString();
        device.OwnerAddress = address.getText().toString();
        // phone
        // email

        Gson gson = new Gson();
        String serializedDevice = gson.toJson(device);
        sendRequest(Command.Request.UPDATE_DEVICE_INFORMATION, serializedDevice, (Message response) -> {
            if (! Command.Response.OK.toString().equals(response.command)) {
                Log.e("EDITACTIVITY", "Could not update device: " + response.argument);
                return;
            }

            deviceViewModel.update(device);

            finish();
        });
    }

    /**
     * When the cancel button is pressed, go back to the previous activity.
     */
    public void onCancelDetails(View view) {
        finish();
    }
}