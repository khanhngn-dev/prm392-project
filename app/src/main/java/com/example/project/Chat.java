package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.adapter.ChatAdapter;
import com.example.project.manager.SocketManager;
import com.example.project.model.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import utils.Auth;
import utils.TimeCalculate;

public class Chat extends AppCompatActivity {

    private ChatAdapter chatAdapter;
    private final List<Message> messages = new ArrayList<>();

    private SocketManager socketManager;

    private RecyclerView chatRecyclerView;
    private EditText inputMessage;
    private AppCompatImageView sendButton;
    private AppCompatImageView imageBack;
    private ProgressBar progressBar;
    private TextView userAvailability;

    private String conversationId;
    private String conversationName;
    private String conversationImage;
    private String otherParticipantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupGetIntent();
        setupViews();
        setupSocket();
        setupRecyclerView();
        setupListeners();
    }

    private void setupGetIntent() {
        try {
            String getCId = getIntent().getStringExtra("conversationId");
            String getCName = getIntent().getStringExtra("conversationName");
            String getCImage = getIntent().getStringExtra("conversationImage");
            String getCOtherParticipantId = getIntent().getStringExtra("otherParticipantId");
            if (getCId == null || getCName == null || getCImage == null || getCOtherParticipantId == null) {
                throw new Exception("No conversation found");
            }

            conversationId = getCId;
            conversationName = getCName;
            conversationImage = getCImage;
            otherParticipantId = getCOtherParticipantId;
        } catch (Exception e) {
            showToast(e.getMessage());
            finish();
        }
    }

    private void setupViews() {
        TextView chatName;

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        inputMessage = findViewById(R.id.inputMessage);
        sendButton = findViewById(R.id.imageSend);
        imageBack = findViewById(R.id.imageBack);
        progressBar = findViewById(R.id.progressBar);
        chatName = findViewById(R.id.textUserName);
        userAvailability = findViewById(R.id.textUserAvailability);

        chatName.setText(conversationName);
        listenForUserStatus(otherParticipantId);
    }

    private void setupListeners() {
        imageBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ConversationList.class);
            startActivity(intent);
        });

        sendButton.setOnClickListener(v -> {
            String content = inputMessage.getText().toString().trim();
            if (!content.isEmpty() && socketManager != null) {
                String messageId = UUID.randomUUID().toString();
                Message message = new Message(messageId, Auth.currentUser().getUid(), content, conversationId, null);

                socketManager.sendMessage(messageId, content, conversationId);
                addMessage(message);
                inputMessage.setText("");
            } else {
                showToast("Message cannot be empty");
            }
        });
    }

    private void setupRecyclerView() {
        chatAdapter = new ChatAdapter(messages, conversationImage);
        chatRecyclerView.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupSocket() {
        SocketManager.getInstance(socketManager -> {
            this.socketManager = socketManager;
            socketManager.connect();
            socketManager.getMessages(conversationId);
            socketManager.setOnNewMessageListener((message) -> {
                String cId = message.getConversationId();
                String uid = Auth.currentUser().getUid();
                boolean isSender = message.getSenderId().equals(uid);
                boolean isCurrentConversation = Objects.equals(cId, conversationId) || Objects.equals(conversationId, "store");
                if (isCurrentConversation && isSender) {
                    messages.stream().filter(m -> m.getId().equals(message.getId())).findFirst().ifPresent(foundMessage -> {
                        foundMessage.setCreatedAt(message.getCreatedAt());
                        int itemIndex = messages.indexOf(foundMessage);
                        runOnUiThread(() -> chatAdapter.notifyItemChanged(itemIndex));
                    });
                } else if (isCurrentConversation) {
                    runOnUiThread(() -> addMessage(message));
                }
            });

            socketManager.setOnGetMessagesListener((messages) -> {
                runOnUiThread(() -> updateMessages(messages));
            });

            return null;
        });
    }

    private void listenForUserStatus(String userId) {
        DatabaseReference statusRef = FirebaseDatabase.getInstance().getReference("user_status").child(userId);
        statusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String state = snapshot.child("state").getValue(String.class);
                    Long lastChanged = snapshot.child("last_changed").getValue(Long.class);
                    updateStatusView(state, lastChanged);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ChatListAdapter", "Status listener cancelled", error.toException());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateStatusView(String state, Long lastChanged) {
        userAvailability.setVisibility(View.VISIBLE);
        if ("online".equals(state)) {
            userAvailability.setText("Online");
            userAvailability.setTextColor(Color.GREEN);
        } else {
            long timeAgo = System.currentTimeMillis() - lastChanged;
            String lastSeen = TimeCalculate.getTimeAgo(timeAgo);
            userAvailability.setText("Last seen: " + lastSeen);
            userAvailability.setTextColor(Color.GRAY);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateMessages(Message[] messages) {
        this.messages.clear();
        this.messages.addAll(List.of(messages));
        chatAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        chatRecyclerView.setVisibility(View.VISIBLE);
        scrollToLastMessage();
    }

    private void addMessage(Message message) {
        messages.add(message);
        chatAdapter.notifyItemInserted(messages.size() - 1);
        chatRecyclerView.scrollToPosition(messages.size() - 1);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void scrollToLastMessage() {
        chatRecyclerView.scrollToPosition(messages.size() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socketManager != null) {
            socketManager.disconnect();
        }
    }
}