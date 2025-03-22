package com.example.tour_nest.model.admin;

import com.example.tour_nest.model.Message;

public class UserChatItem {
    private String userId;
    private String email;
    private Message lastMessage;
    private String avatarUrl;

    public UserChatItem(String userId, String email, Message lastMessage) {
        this.userId = userId;
        this.email = email;
        this.lastMessage = lastMessage;
        this.avatarUrl = null;
    }

    public UserChatItem(String userId, String email, Message lastMessage, String avatarUrl) {
        this.userId = userId;
        this.email = email;
        this.lastMessage = lastMessage;
        this.avatarUrl = avatarUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}