package com.example.project.manager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserStatusManager {
    private static final String STATUS_NODE = "user_status";
    private final DatabaseReference userStatusRef;
    private ValueEventListener connectedListener;

    public UserStatusManager(String userId) {
        userStatusRef = FirebaseDatabase.getInstance().getReference(STATUS_NODE).child(userId);
        userStatusRef.keepSynced(true);
    }

    public void startListening() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedListener = connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = Boolean.TRUE.equals(snapshot.getValue(Boolean.class));
                if (connected) {
                    updateStatus("online");
                    addOnDisconnectListener();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("UserStatusManager", "Listener was cancelled", error.toException());
            }
        });
    }

    public void stopListening() {
        if (connectedListener != null) {
            FirebaseDatabase.getInstance().getReference(".info/connected").removeEventListener(connectedListener);
        }
    }

    private void addOnDisconnectListener() {
        Map<String, Object> offlineStatus = new HashMap<>();
        offlineStatus.put("state", "offline");
        offlineStatus.put("last_changed", ServerValue.TIMESTAMP);
        userStatusRef.onDisconnect().setValue(offlineStatus);
    }

    private void updateStatus(String status) {
        Map<String, Object> onlineStatus = new HashMap<>();
        onlineStatus.put("state", status);
        onlineStatus.put("last_changed", ServerValue.TIMESTAMP);
        userStatusRef.setValue(onlineStatus);
    }

    public void setOffline() {
        updateStatus("offline");
    }
}