package com.example.pocketalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.pocketalert.MainActivity.EDIT_DETAILS_ACTIVITY_REQUEST_CODE;

public class DetailActivity extends AppCompatActivity {

    private int id;
    private TextView idView, nameView, addressView, phoneView, emailView, bdayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        idView = findViewById(R.id.idDetail);
        nameView = findViewById(R.id.nameDetail);
        addressView = findViewById(R.id.addressDetail);
        phoneView = findViewById(R.id.phoneDetail);
        emailView = findViewById(R.id.emailDetail);
        bdayView = findViewById(R.id.bdayDetail);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            id = b.getInt("id");
        } else {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            finish();
        }

        idView.setText(Integer.toString(id));

    }

    public void onEdit(View view) {
        Intent intent = new Intent(this, EditDetailsActivity.class);
        startActivityForResult(intent, EDIT_DETAILS_ACTIVITY_REQUEST_CODE);
    }
}