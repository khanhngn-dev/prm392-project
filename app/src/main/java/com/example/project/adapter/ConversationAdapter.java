package com.example.project.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.model.Conversation;

import java.text.SimpleDateFormat;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private final List<Conversation> conversations;
    private final OnConversationClickListener listener;

    public interface OnConversationClickListener {
        void onConversationClick(Conversation conversation);
    }

    public ConversationAdapter(List<Conversation> conversations, OnConversationClickListener listener) {
        this.conversations = conversations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation conversation = conversations.get(position);
        holder.bind(conversation);
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder {
        ImageView conversationImage;
        TextView conversationName;
        TextView lastMessage;
        TextView lastMessageSent;

        ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            conversationImage = itemView.findViewById(R.id.conversationImage);
            conversationName = itemView.findViewById(R.id.conversationName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            lastMessageSent = itemView.findViewById(R.id.lastMessageSent);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onConversationClick(conversations.get(position));
                }
            });
        }

        void bind(Conversation conversation) {
            conversationName.setText(conversation.getName());
            lastMessage.setText(conversation.getLastMessage());

            if (conversation.getLastMessageSent() != null) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
                String date = sdf.format(conversation.getLastMessageSent());
                lastMessageSent.setText(date);
            }

            // Sử dụng Glide để tải và hiển thị ảnh
            Glide.with(itemView.getContext())
                    .load(conversation.getImage())
                    .circleCrop()
                    .placeholder(R.drawable.default_avatar)
                    .into(conversationImage);
        }
    }
}