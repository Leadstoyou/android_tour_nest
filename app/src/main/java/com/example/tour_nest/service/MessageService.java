package com.example.tour_nest.service;

import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.base.GenericRepository;
import com.example.tour_nest.model.Message;
import com.example.tour_nest.model.User;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService<T> {
    private static final GenericRepository<Message> messageRepository = new GenericRepository<>("messages", Message.class);
    private FirebaseCallback<T> callback;

    public MessageService() {
    }

    public static GenericRepository<Message> getRef() {
        return messageRepository;
    }

    public void onResult(FirebaseCallback<T> callback) {
        this.callback = callback;
    }

    // Gửi tin nhắn từ user hoặc admin
    public static void sendMessage(Message message) {
        MessageService<Boolean> service = new MessageService<>();
        try {
            messageRepository.create(message);
            if (service.callback != null) service.callback.onSuccess(true);
        } catch (Exception e) {
            if (service.callback != null) service.callback.onFailure(e);
        }
    }

    public static MessageService<List<String>> getUserListForAdmin(String adminId) {
        MessageService<List<String>> service = new MessageService<>();
        try {
            messageRepository.getAll(new FirebaseCallback<List<Message>>() {
                @Override
                public void onSuccess(List<Message> messages) {
                    List<String> userIds = messages.stream()
                            .filter(m -> m.getSenderId().equals(adminId) || m.getReceiverId().equals(adminId))
                            .map(m -> m.getSenderId().equals(adminId) ? m.getReceiverId() : m.getSenderId())
                            .distinct()
                            .collect(Collectors.toList());
                    if (service.callback != null) service.callback.onSuccess(userIds);
                }

                @Override
                public void onFailure(Exception e) {
                    if (service.callback != null) service.callback.onFailure(e);
                }
            });
        } catch (Exception e) {
            if (service.callback != null) service.callback.onFailure(e);
        }
        return service;
    }

    public static MessageService<List<Message>> getConversationRealtime(String userId1, String userId2) {
        MessageService<List<Message>> service = new MessageService<>();
        try {
            messageRepository.searchRealtime("senderId", userId1, new FirebaseCallback<List<Message>>() {
                @Override
                public void onSuccess(List<Message> senderMessages) {
                    messageRepository.searchRealtime("senderId", userId2, new FirebaseCallback<List<Message>>() {
                        @Override
                        public void onSuccess(List<Message> receiverMessages) {
                            List<Message> conversation = senderMessages.stream()
                                    .filter(m -> m.getReceiverId().equals(userId2))
                                    .collect(Collectors.toList());
                            conversation.addAll(receiverMessages.stream()
                                    .filter(m -> m.getReceiverId().equals(userId1))
                                    .collect(Collectors.toList()));
                            conversation.sort(Comparator.comparingLong(Message::getTimestamp));
                            if (service.callback != null) service.callback.onSuccess(conversation);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            if (service.callback != null) service.callback.onFailure(e);
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    if (service.callback != null) service.callback.onFailure(e);
                }
            });
            // Để hỗ trợ realtime, cần thay đổi GenericRepository để dùng addValueEventListener
        } catch (Exception e) {
            if (service.callback != null) service.callback.onFailure(e);
        }
        return service;
    }

    public static MessageService<List<Message>> getMessagesByUserRealtime(String userId) {
        MessageService<List<Message>> service = new MessageService<>();
        try {
            messageRepository.searchRealtime("senderId", userId, new FirebaseCallback<List<Message>>() {
                @Override
                public void onSuccess(List<Message> sentMessages) {
                    messageRepository.searchRealtime("receiverId", userId, new FirebaseCallback<List<Message>>() {
                        @Override
                        public void onSuccess(List<Message> receivedMessages) {
                            List<Message> allMessages = new ArrayList<>(sentMessages);
                            allMessages.addAll(receivedMessages);
                            allMessages.sort(Comparator.comparingLong(Message::getTimestamp));
                            if (service.callback != null) service.callback.onSuccess(allMessages);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            if (service.callback != null) service.callback.onFailure(e);
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    if (service.callback != null) service.callback.onFailure(e);
                }
            });
        } catch (Exception e) {
            if (service.callback != null) service.callback.onFailure(e);
        }
        return service;
    }

    public static MessageService<Message> getById(String id) {
        MessageService<Message> service = new MessageService<>();
        try {
            messageRepository.get(id, new FirebaseCallback<Message>() {
                @Override
                public void onSuccess(Message result) {
                    if (service.callback != null) service.callback.onSuccess(result);
                }

                @Override
                public void onFailure(Exception e) {
                    if (service.callback != null) service.callback.onFailure(e);
                }
            });
        } catch (Exception e) {
            if (service.callback != null) service.callback.onFailure(e);
        }
        return service;
    }
}