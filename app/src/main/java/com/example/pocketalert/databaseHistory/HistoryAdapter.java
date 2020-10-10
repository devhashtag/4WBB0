package com.example.pocketalert.databaseHistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketalert.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    private List<History> histories = new ArrayList<>();
    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item,parent,false);
        return new HistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        History currentHistory = histories.get(position);
        String date = "Date:"+currentHistory.getDate();
        String userID = "UserID: "+currentHistory.getUserId();
        String locationX = "X coordinate: "+currentHistory.getLocationX();
        String locationY = "Y coordinate: "+currentHistory.getLocationY();
        String ongoing = "Is still ongoing: "+currentHistory.getOnGoing();
        holder.textViewID.setText(String.valueOf(currentHistory.getId()));
        holder.textViewDate.setText(date);
        holder.textViewUserID.setText(userID);
        holder.textViewName.setText(currentHistory.getName());
        holder.textViewLocX.setText(locationX);
        holder.textViewLocY.setText(locationY);
        holder.textViewOngoing.setText(ongoing);
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public void setHistories(List<History> histories){
        this.histories =  histories;
        notifyDataSetChanged();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {
        private TextView textViewID;
        private TextView textViewDate;
        private TextView textViewUserID;
        private TextView textViewName;
        private TextView textViewLocX;
        private TextView textViewLocY;
        private TextView textViewOngoing;


        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            textViewID= itemView.findViewById(R.id.text_view_id);
            textViewDate= itemView.findViewById(R.id.text_view_date);
            textViewUserID= itemView.findViewById(R.id.text_view_UserID);
            textViewName = itemView.findViewById(R.id.text_view_title);
            textViewLocX= itemView.findViewById(R.id.text_view_LocationX);
            textViewLocY= itemView.findViewById(R.id.text_view_LocationY);
            textViewOngoing= itemView.findViewById(R.id.text_view_onGoing);

        }
    }
}