package com.example.project.model;

import java.util.Date;

public class Conversation {
    private String id;
    private String name;
    private String image;
    private String lastMessage;
    private Date lastMessageSent;
    private String otherParticipantId;
    private Date createdAt;

    public Conversation() {
    }

    public Conversation(String id, String name, String image, String lastMessage, Date lastMessageSent, String otherParticipantId, Date createdAt) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.lastMessage = lastMessage;
        this.lastMessageSent = lastMessageSent;
        this.otherParticipantId = otherParticipantId;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Date getLastMessageSent() {
        return lastMessageSent;
    }

    public void setLastMessageSent(Date lastMessageSent) {
        this.lastMessageSent = lastMessageSent;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getOtherParticipantId() {
        return otherParticipantId;
    }

    public void setOtherParticipantId(String otherParticipantId) {
        this.otherParticipantId = otherParticipantId;
    }
}