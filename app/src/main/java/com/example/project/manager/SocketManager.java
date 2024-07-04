package com.example.project.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.project.model.Conversation;
import com.example.project.model.Message;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.socket.client.IO;
import io.socket.client.Socket;
import utils.Auth;

public class SocketManager {
    private static SocketManager instance;
    private Socket socket;

    private SocketManager(String token) {
        try {
            IO.Options options = new IO.Options();
            Map<String, String> authorization = new HashMap<>();
            authorization.put("authorization", "Bearer " + token);

            options.auth = authorization;
            socket = IO.socket("https://prm392.tripllery.com", options);
        } catch (Exception e) {
            Log.e("SocketManager", "Error creating socket: " + e.getMessage());
        }
    }

    public static synchronized void getInstance(Function<SocketManager, Void> callback, Context context) {
        FirebaseUser user = Auth.currentUser();
        user.getIdToken(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String idToken = task.getResult().getToken();
                if (idToken == null) {
                    Log.e("FirebaseAuth", "Failed to get user id token");
                    Toast.makeText(context, "Failed to get user id token", Toast.LENGTH_SHORT).show();
                    return;
                }

                String uid = user.getUid();

                SocketManager socketManager = getInstance(uid);
                callback.apply(socketManager);
            } else {
                Log.e("FirebaseAuth", "Failed to get user id token");
                Toast.makeText(context, "Failed to get user id token", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static SocketManager getInstance(String token) {
        if (instance == null) {
            instance = new SocketManager(token);
        }
        return instance;
    }

    public static void resetClient() {
        instance = null;
    }

    public void connect() {
        socket.connect();
    }

    public void disconnect() {
        socket.disconnect();
    }

    public Socket getSocket() {
        return socket;
    }

    public void sendMessage(String id, String message, String conversationId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("message", message);
            jsonObject.put("conversationId", conversationId);
            socket.emit("new_message", jsonObject);
        } catch (JSONException e) {
            Log.e("SocketManager", "Error sending message: " + e.getMessage());
        }
    }

    public void getConversations() {
        socket.emit("get_conversations");
    }

    public void getMessages(String conversationId) {
        socket.emit("get_messages", conversationId);
    }

    public interface OnNewMessageListener {
        void onNewMessage(Message message);
    }

    public interface OnGetConversationsListener {
        void onGetConversations(Conversation[] conversations);
    }

    public interface OnNewConversationListener {
        void onNewConversation(Conversation conversation);
    }

    public interface OnGetMessagesListener {
        void onGetMessages(Message[] messages);
    }

    public void setOnNewMessageListener(final OnNewMessageListener listener) {
        socket.on("new_message", args -> {
            if (args[0] != null) {
                Gson gson = new Gson();
                Message message = gson.fromJson(args[0].toString(), Message.class);
                listener.onNewMessage(message);
            }
        });
    }

    public void setOnGetConversationsListener(final OnGetConversationsListener listener) {
        socket.on("get_conversations", args -> {
            if (args[0] != null) {
                Gson gson = new Gson();
                Conversation[] conversations = gson.fromJson(args[0].toString(), Conversation[].class);
                listener.onGetConversations(conversations);
            }
        });
    }

    public void setOnNewConversationListener(final OnNewConversationListener listener) {
        socket.on("new_conversation", args -> {
            if (args[0] != null) {
                Gson gson = new Gson();
                Conversation conversation = gson.fromJson(args[0].toString(), Conversation.class);
                listener.onNewConversation(conversation);
                socket.emit("subscribe_conversation", conversation.getId());
            }
        });
    }

    public void setOnGetMessagesListener(final OnGetMessagesListener listener) {
        socket.on("get_messages", args -> {
            if (args[0] != null) {
                Gson gson = new Gson();
                Message[] messages = gson.fromJson(args[0].toString(), Message[].class);
                listener.onGetMessages(messages);
            }
        });
    }
}