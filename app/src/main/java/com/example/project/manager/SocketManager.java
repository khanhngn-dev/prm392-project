package com.example.project.manager;

import android.content.Context;
import android.util.Log;

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
import io.socket.emitter.Emitter;
import utils.Auth;

public class SocketManager {
    private static SocketManager instance;
    private Socket socket;

    private SocketManager(String userId, String email) {
        try {
            IO.Options options = new IO.Options();
            Map<String, String> authorization = new HashMap<>();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("userId", userId);
            jsonObject.put("email", email);

            authorization.put("authorization", jsonObject.toString());

            options.auth = authorization;
            socket = IO.socket("https://prm392.tripllery.com", options);
        } catch (Exception e) {
            Log.e("SocketManager", "Error creating socket: " + e.getMessage());
        }
    }

    public static synchronized void getInstance(Function<SocketManager, Void> callback, Context context) {
        if (instance != null) {
            callback.apply(instance);
            return;
        }

        FirebaseUser user = Auth.currentUser();
        if (user != null) {
            String uid = user.getUid();
            String email = user.getEmail();

            SocketManager socketManager = getInstance(uid, email);
            callback.apply(socketManager);
        }
    }

    public static SocketManager getInstance(String userId, String email) {
        instance = new SocketManager(userId, email);

        return instance;
    }

    public static void resetClient() {
        if (instance != null) {
            instance.disconnect();
            instance = null;
        }
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

    public void readMessages(String conversationId) {
        socket.emit("read_messages", conversationId);
    }

    public void getUnreads() {
        socket.emit("get_unreads");
    }

    public void subscribeConversation(String conversationId) {
        socket.emit("subscribe_conversation", conversationId);
    }

    public interface OnNewMessageListener {
        void onNewMessage(Message message);
    }

    public interface OnUpdateUnreadMessagesListener {
        void onUpdateUnreadMessages(int unreadMessages);
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

    public Emitter.Listener setOnNewMessageListener(final OnNewMessageListener listener) {
        Emitter.Listener fnListener = args -> {
            if (args[0] != null) {
                Gson gson = new Gson();
                Message message = gson.fromJson(args[0].toString(), Message.class);
                listener.onNewMessage(message);
            }
        };

        socket.on("new_message", fnListener);

        return fnListener;
    }

    public void offEventListener(String eventName, Emitter.Listener listener) {
        socket.off(eventName, listener);
    }

    public Emitter.Listener setOnUpdateUnreadMessagesListener(final OnUpdateUnreadMessagesListener listener) {
        Emitter.Listener fnListener = args -> {
            if (args[0] != null) {
                listener.onUpdateUnreadMessages((int) args[0]);
            }
        };

        socket.on("update_unread", fnListener);

        return fnListener;
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