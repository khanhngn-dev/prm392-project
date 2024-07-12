package com.example.project;

import androidx.annotation.NonNull;

import com.example.project.manager.SocketManager;
import com.example.project.manager.UserStatusManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import utils.Auth;

public class Application extends android.app.Application {
    private UserStatusManager userStatusManager;
    private SocketManager socketManager;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        setupUserStatusManager();
        setupSocket();
    }

    private void setupUserStatusManager() {
        Auth.auth.addAuthStateListener((@NonNull FirebaseAuth firebaseAuth) -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                userStatusManager = new UserStatusManager(user.getUid());
                userStatusManager.startListening();
            } else if (userStatusManager != null) {
                userStatusManager.setOffline();
                userStatusManager.stopListening();
                userStatusManager = null;
            }
        });
    }

    private void setupSocket() {
        Auth.auth.addAuthStateListener((@NonNull FirebaseAuth firebaseAuth) -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                SocketManager.getInstance(socketManager -> {
                    this.socketManager = socketManager;
                    socketManager.connect();
                    socketManager.setOnNewConversationListener((conversation) -> {
                        socketManager.subscribeConversation(conversation.getId());
                    });

                    return null;
                }, this);
            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (userStatusManager != null) {
            userStatusManager.setOffline();
            userStatusManager.stopListening();
        }

        if (socketManager != null) {
            socketManager.disconnect();
        }
    }
}