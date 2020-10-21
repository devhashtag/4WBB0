package com.example.pocketalert;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketalert.data.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder> {
    public interface Listener {
        void onClick(String id);
    }

    private static final String TAG = "DeviceListAdapter";
    private final LayoutInflater inflater;
    private List<Device> devices;

    private Listener onClick;
    private Listener onRemove;

    public DeviceListAdapter(Context context, Listener onClick, Listener onRemove) {
        inflater = LayoutInflater.from(context);
        this.onRemove = onRemove;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.users_recyclerview_item, parent, false);
        return new DeviceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        if (devices != null) {
            Device current = devices.get(position);
            String id = current.Id;
            String name = current.OwnerFirstName;

            if (name == null || name.length() == 0) {
                holder.deviceView.setText("<Enter a name>");
            } else {
                holder.deviceView.setText(name);
            }

            holder.editButton.setOnClickListener((View v) -> {
                onClick.onClick(id);
            });
            holder.deleteButton.setOnClickListener((View v) -> {
                onRemove.onClick(id);
            });
        }
    }

    @Override
    public int getItemCount() {
        return devices == null ? 0 : devices.size();
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
        notifyDataSetChanged();
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        private final TextView deviceView;
        private final Button editButton;
        private final Button deleteButton;

        private DeviceViewHolder(View itemView) {
            super(itemView);

            deviceView = itemView.findViewById(R.id.userView);
            editButton = itemView.findViewById(R.id.viewUserButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
