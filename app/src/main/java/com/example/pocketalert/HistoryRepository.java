package com.example.pocketalert;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class HistoryRepository {
    private HistoryDao historyDao;
    private LiveData<List<History>> allHistoryMessages;

    public HistoryRepository(Application application){
        HistoryDataBase database = HistoryDataBase.getInstance(application);
        historyDao = database.HistoryDao();
        allHistoryMessages = historyDao.getAllHistory();
    }

    public void insert(History history){
        new InsertHistoryAsyncTask(historyDao).execute(history);
    }

    public void update(History history){
        new UpdateHistoryAsyncTask(historyDao).execute(history);
    }

    public void delete(History history){
        new DeleteHistoryAsyncTask(historyDao).execute(history);
    }

    public void deleteAllHistoryListings(){
        new DeleteHistoryAsyncTask(historyDao).execute();
    }

    public LiveData<List<History>> getAllHistoryMessages() {
        return allHistoryMessages;
    }

    private static class InsertHistoryAsyncTask extends AsyncTask<History,Void,Void> {
        private HistoryDao historyDao;

        private InsertHistoryAsyncTask(HistoryDao historyDao){
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(History... histories){
            historyDao.insert(histories[0]);
            return null;
        }
    }

    private static class UpdateHistoryAsyncTask extends AsyncTask<History,Void,Void> {
        private HistoryDao historyDao;

        private UpdateHistoryAsyncTask(HistoryDao historyDao){
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(History... histories){
            historyDao.update(histories[0]);
            return null;
        }
    }

    private static class DeleteHistoryAsyncTask extends AsyncTask<History,Void,Void> {
        private HistoryDao historyDao;

        private DeleteHistoryAsyncTask(HistoryDao historyDao){
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(History... histories){
            historyDao.insert(histories[0]);
            return null;
        }
    }

    private static class DeleteAllHistoryAsyncTask extends AsyncTask<Void,Void,Void> {
        private HistoryDao historyDao;

        private DeleteAllHistoryAsyncTask(HistoryDao historyDao){
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(Void... voids){
            historyDao.deleteAllHistory();
            return null;
        }
    }
}