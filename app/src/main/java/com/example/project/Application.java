package com.example.project;

import androidx.annotation.NonNull;

import com.example.project.manager.UserStatusManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import utils.Auth;

public class Application extends android.app.Application {
    private UserStatusManager userStatusManager;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        setupUserStatusManager();
    }

    private void setupUserStatusManager() {
        Auth.auth.addAuthStateListener((@NonNull FirebaseAuth firebaseAuth) -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                userStatusManager = new UserStatusManager(user.getUid());
                userStatusManager.startListening();
            } else if (userStatusManager != null) {
                userStatusManager.stopListening();
                userStatusManager = null;
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
    }
}