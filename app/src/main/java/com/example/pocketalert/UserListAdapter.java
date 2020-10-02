package com.example.pocketalert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketalert.database.User;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private final LayoutInflater inflater;
    private List<User> users; // Cached copy of users
    UserListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.users_recyclerview_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        if (users != null) {
            User current = users.get(position);
            holder.userItemView.setText(current.getName());
        } else {
            // Covers the case of data not being ready yet.
            holder.userItemView.setText(R.string.no_name);
        }
    }

    void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // users has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (users != null)
            return users.size();
        else return 0;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView userItemView;

        private UserViewHolder(View itemView) {
            super(itemView);
            userItemView = itemView.findViewById(R.id.userView);
        }
    }
}
