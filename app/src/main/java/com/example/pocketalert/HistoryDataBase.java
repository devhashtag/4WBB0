package com.example.pocketalert;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = History.class, version = 1)
public abstract class HistoryDataBase extends RoomDatabase {

    private static HistoryDataBase instance;

    public abstract HistoryDao HistoryDao();

    public static synchronized HistoryDataBase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    HistoryDataBase.class,"History_DataBase")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    // first time the database is opened this will run
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDataBaseAsyncTask(instance).execute();
        }
    };

    private static class PopulateDataBaseAsyncTask extends AsyncTask<Void,Void,Void>{
        private HistoryDao historyDao;
        private PopulateDataBaseAsyncTask(HistoryDataBase db){
            historyDao = db.HistoryDao();
        }
        @Override
        protected Void doInBackground(Void... voids){
            historyDao.insert(new History(1,"6-10-2020,17:22","123","Merlijn Sevenhuijsen"));
            return null;
        }
    }
}