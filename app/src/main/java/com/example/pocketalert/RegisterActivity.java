package com.example.pocketalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.pocketalert.R;
import com.example.pocketalert.RegisterInstructionsActivity;
import com.example.pocketalert.configuration.Command;
import com.example.pocketalert.connect.ConnectedActivity;
import com.example.pocketalert.connect.Message;
import com.example.pocketalert.data.Device;
import com.example.pocketalert.data.DeviceViewModel;
import com.google.gson.Gson;

public class RegisterActivity extends ConnectedActivity {
    private EditText idInput;
    private Button cancelButton;
    private DeviceViewModel deviceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        idInput = findViewById(R.id.connectInput);
        cancelButton = findViewById(R.id.cancelConnectButton);
    }

    /**
     * When the connect button is pressed, go back to the previous activity. If an ID was entered, that gets sent back as an extra to be added to the database.
     */
    public void onConnect(View view) {
        String id = idInput.getText().toString();

        cancelButton.setEnabled(false);

        sendRequest(Command.Request.ADD_DEVICE, id, (Message response) -> {
            if (! Command.Response.OK.toString().equals(response.command)) {
                Toast.makeText(this, "Id does not belong to a device", Toast.LENGTH_SHORT).show();
                cancelButton.setEnabled(true);
                return;
            }

            Gson gson = new Gson();
            Device device = gson.fromJson(response.argument, Device.class);

            deviceViewModel.insert(device);

            Intent reply = new Intent();
            setResult(RESULT_OK, reply);

            finish();
        });
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