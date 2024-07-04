package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.adapter.ConversationAdapter;
import com.example.project.manager.SocketManager;
import com.example.project.model.Conversation;
import com.example.project.model.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationList extends AppCompatActivity {

    private ConversationAdapter conversationAdapter;
    private final List<Conversation> conversations = new ArrayList<>();

    private SocketManager socketManager;

    private ProgressBar progressBar;
    private RecyclerView conversationRecyclerView;
    private AppCompatImageView imageHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_conversation_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupViews();
        setupRecyclerView();
        setupListeners();
        setupSocket();
    }

    private void setupViews() {
        progressBar = findViewById(R.id.progressBar);
        imageHome = findViewById(R.id.imageHome);
        conversationRecyclerView = findViewById(R.id.conversationRecyclerView);
    }

    private void setupRecyclerView() {
        conversationAdapter = new ConversationAdapter(conversations, conversation -> {
            Intent intent = new Intent(this, Chat.class);
            intent.putExtra("conversationId", conversation.getId());
            intent.putExtra("conversationName", conversation.getName());
            intent.putExtra("conversationImage", conversation.getImage());
            intent.putExtra("otherParticipantId", conversation.getOtherParticipantId());
            startActivity(intent);
        });
        conversationRecyclerView.setAdapter(conversationAdapter);
        conversationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        imageHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
        });
    }

    private void setupSocket() {
        SocketManager.getInstance(socketManager -> {
            this.socketManager = socketManager;
            socketManager.connect();
            socketManager.getConversations();
            socketManager.setOnNewMessageListener((message) -> {
                runOnUiThread(() -> updateConversation(message));
            });
            socketManager.setOnGetConversationsListener((conversations) -> {
                runOnUiThread(() -> updateConversations(conversations));
            });
            socketManager.setOnNewConversationListener((conversation) -> {
                runOnUiThread(() -> {
                    conversations.add(0, conversation);
                    conversationAdapter.notifyItemInserted(0);
                });
            });

            return null;
        }, this);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateConversations(Conversation[] conversations) {
        this.conversations.clear();
        this.conversations.addAll(List.of(conversations));
        conversationAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        conversationRecyclerView.setVisibility(View.VISIBLE);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateConversation(Message message) {
        String conversationId = message.getConversationId();
        String lastMessage = message.getContent();
        Date lastMessageSent = message.getCreatedAt();

        Conversation conversation = findConversationById(conversationId);
        if (conversation != null) {
            conversation.setLastMessage(lastMessage);
            conversation.setLastMessageSent(lastMessageSent);
            conversationAdapter.notifyDataSetChanged();
        } else {
            Conversation newConversation = new Conversation();
            newConversation.setId(conversationId);
            newConversation.setLastMessage(lastMessage);
            newConversation.setLastMessageSent(lastMessageSent);
            newConversation.setName("New Conversation");
            newConversation.setCreatedAt(new Date());
            conversations.add(newConversation);
            conversationAdapter.notifyItemInserted(conversations.size() - 1);
        }
    }

    private Conversation findConversationById(String id) {
        for (Conversation conversation : conversations) {
            if (conversation.getId().equals(id)) {
                return conversation;
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socketManager != null) {
            socketManager.disconnect();
        }
    }
}