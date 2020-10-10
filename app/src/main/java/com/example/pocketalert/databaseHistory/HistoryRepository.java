package com.example.pocketalert.databaseHistory;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.pocketalert.database.User;
import com.example.pocketalert.database.UserRoomDatabase;

import java.util.ArrayList;
import java.util.List;

public class HistoryRepository {
    private HistoryDao historyDao;
    private LiveData<List<History>> allHistoryMessages;

    HistoryRepository(Application application){
        HistoryDataBase database = HistoryDataBase.getDatabase(application);
        historyDao = database.historyDao();
        allHistoryMessages = historyDao.getAllHistory();
    }

    public void insert(History history){
        HistoryDataBase.databaseWriteExecutor.execute(() -> historyDao.insert(history));
    }

    public void update(History history){
        HistoryDataBase.databaseWriteExecutor.execute(() -> historyDao.update(history));
    }

    public void delete(History history){
        HistoryDataBase.databaseWriteExecutor.execute(() -> historyDao.delete(history));
    }

    public void deleteAllHistoryListings(){
        HistoryDataBase.databaseWriteExecutor.execute(() -> historyDao.deleteAllHistory());
    }

    public LiveData<List<History>> getAllHistoryMessages() {
        return allHistoryMessages;
    }

    List<History> getHistory(int id_history) {
        final ArrayList<History>[] history = new ArrayList[1];
        HistoryDataBase.databaseWriteExecutor.execute(() -> history[0] = (ArrayList<History>) historyDao.getHistory(id_history));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return history[0];
    }
}