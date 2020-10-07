package com.example.pocketalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private HistoryViewModel historyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(HistoryViewModel.class);

        historyViewModel.getAllHistoryMessages().observe(this, new Observer<List<History>>() {
            @Override
            public void onChanged(List<History> histories) {
                // update RecyclerView
                Toast.makeText(HistoryActivity.this,"This has changed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}