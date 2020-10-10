package com.example.pocketalert.databaseHistory;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pocketalert.database.User;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private HistoryRepository repository;

    private LiveData<List<History>> allHistoryMessages;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new HistoryRepository(application);
        allHistoryMessages = repository.getAllHistoryMessages();
    }

    public void insert(History history){
        repository.insert(history);
    }

    public void update(History history){
        repository.update(history);
    }

    public LiveData<List<History>> getAllHistoryMessages(){
        return allHistoryMessages;
    }

    public void delete(History history){
        repository.delete(history);
    }

    public void deleteAllHistoryMessages(History history){
        repository.deleteAllHistoryListings();
    }

}