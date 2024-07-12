package com.example.project;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.manager.SocketManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.FirebaseDatabase;

import io.socket.emitter.Emitter;
import utils.Auth;

public abstract class Base extends AppCompatActivity {
    FirebaseDatabase database;
    SocketManager socketManager;
    private Emitter.Listener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        setupSocket();
    }

    private void setupSocket() {
        SocketManager.getInstance(socketManager -> {
            this.socketManager = socketManager;
            this.listener = socketManager.setOnNewMessageListener((message) -> {
                runOnUiThread(() -> {
                    socketManager.getUnreads();
                    String userId = Auth.currentUser().getUid();
                    String senderId = message.getSenderId();
                    boolean isSender = userId.equals(senderId);
                    if (!isSender) {
                        Toast.makeText(this, "Store message: " + message.getContent(), Toast.LENGTH_SHORT).show();
                    }
                });
            });

            return null;
        }, this);
    }

    public abstract void onMapReady(@NonNull GoogleMap googleMap);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socketManager != null) {
            socketManager.offEventListener("new_message", listener);
        }
    }
}