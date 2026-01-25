package com.carecircle.communication.service.impl;

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

    public ChatServiceImpl(
            ChatRoomRepository chatRoomRepository,
            ChatParticipantRepository chatParticipantRepository,
            ChatMessageRepository chatMessageRepository, 
            NotificationService notificationService
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.notificationService = notificationService;
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
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomId(roomId);
        chatMessage.setSenderId(senderId);
        chatMessage.setContent(message);
        chatMessage.setMessageType(MessageType.TEXT);

        chatMessageRepository.save(chatMessage);

        // TEMPORARY: notify sender only (will improve later)
        notificationService.createNotification(
                senderId,
                "CHAT",
                "New message sent"
        );
    }

}
