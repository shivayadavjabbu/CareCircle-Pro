package com.carecircle.communication.service.impl;

import com.carecircle.communication.exception.ChatBlockedException;
import com.carecircle.communication.model.chat.ChatMessage;
import com.carecircle.communication.service.interfaces.NotificationService;
import com.carecircle.communication.model.chat.ChatParticipant;
import com.carecircle.communication.model.chat.ChatRoom;
import com.carecircle.communication.model.chat.ChatRoomType;
import com.carecircle.communication.model.chat.MessageType;
import com.carecircle.communication.repository.chat.ChatMessageRepository;
import com.carecircle.communication.repository.chat.ChatParticipantRepository;
import com.carecircle.communication.repository.chat.ChatRoomRepository;
import com.carecircle.communication.service.interfaces.ChatService;
import com.carecircle.communication.repository.chat.BlockedUserRepository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final NotificationService notificationService;
    private final BlockedUserRepository blockedUserRepository;

    public ChatServiceImpl(
            ChatRoomRepository chatRoomRepository,
            ChatParticipantRepository chatParticipantRepository,
            ChatMessageRepository chatMessageRepository, 
            NotificationService notificationService, 
            BlockedUserRepository blockedUserRepository
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.notificationService = notificationService;
        this.blockedUserRepository = blockedUserRepository;
    }

    @Override
    public UUID createChatRoom(UUID bookingId) {
        ChatRoom room = new ChatRoom();
        room.setType(ChatRoomType.DIRECT);
        room.setBookingId(bookingId);

        ChatRoom savedRoom = chatRoomRepository.save(room);
        return savedRoom.getId();
    }

    @Override
    public void addParticipant(UUID roomId, UUID userId) {
        ChatParticipant participant = new ChatParticipant();
        participant.setRoomId(roomId);
        participant.setUserId(userId);

        chatParticipantRepository.save(participant);
    }

    @Override
    public void sendMessage(UUID roomId, UUID senderId, String message) {

        // Fetch participants once
        var participants = chatParticipantRepository.findByRoomId(roomId);

        // Enforce block rule: sender must not be blocked by any participant
        boolean isBlocked = participants.stream()
                .map(ChatParticipant::getUserId)
                .anyMatch(userId ->
                        blockedUserRepository.existsByBlockerIdAndBlockedId(userId, senderId)
                );

        if (isBlocked) {
            throw new ChatBlockedException("Message cannot be sent. You are blocked by a participant.");
        }

        // Save message
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomId(roomId);
        chatMessage.setSenderId(senderId);
        chatMessage.setContent(message);
        chatMessage.setMessageType(MessageType.TEXT);

        chatMessageRepository.save(chatMessage);

        // Notify other participants who have NOT blocked the sender
        participants.stream()
                .map(ChatParticipant::getUserId)
                .filter(userId -> !userId.equals(senderId))
                .filter(userId ->
                        !blockedUserRepository.existsByBlockerIdAndBlockedId(userId, senderId)
                )
                .forEach(userId ->
                        notificationService.createNotification(
                                userId,
                                "CHAT",
                                "New message received"
                        )
                );
    }



}
