package com.example.project.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import utils.Auth;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private final List<Message> messages;
    private final String image;

    public ChatAdapter(List<Message> messages, String image) {
        this.messages = messages;
        this.image = image;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_sent_message, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_received_message, parent, false);
            CircleImageView profileImage = view.findViewById(R.id.imageSenderProfile);

            // Set the image url to the profile image
            Glide.with(view).load(image).circleCrop().into(profileImage);
        }

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        String userId = Auth.currentUser().getUid();
        if (message.getSenderId() != null && message.getSenderId().equals(userId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView dateTime;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.textMessage);
            dateTime = itemView.findViewById(R.id.textDateAndTime);
        }

        @SuppressLint("SetTextI18n")
        void bind(Message message) {
            messageText.setText(message.getContent());

            Date messageSent = message.getCreatedAt();
            if (messageSent == null) {
                dateTime.setText("Sending...");
            } else {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
                String date = sdf.format(message.getCreatedAt());
                dateTime.setText(date);
            }
        }
    }
}