package com.example.project.model;

import java.util.Date;

public class Message {
    private String id;
    private String senderId;
    private String content;
    private String conversationId;
    private Date createdAt;

    public Message() {
    }

    public Message(String id, String senderId, String content, String conversationId, Date createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.content = content;
        this.conversationId = conversationId;
        this.createdAt = createdAt;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}