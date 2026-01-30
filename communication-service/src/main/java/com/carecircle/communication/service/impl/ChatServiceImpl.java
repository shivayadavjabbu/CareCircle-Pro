package com.carecircle.communication.service.impl;

import com.carecircle.communication.dto.response.ChatMessageResponse;
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
import com.carecircle.communication.service.interfaces.BlockService;
import com.carecircle.communication.service.interfaces.ChatService;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final NotificationService notificationService;
    private final BlockService blockService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatServiceImpl(
            ChatRoomRepository chatRoomRepository,
            ChatParticipantRepository chatParticipantRepository,
            ChatMessageRepository chatMessageRepository, 
            NotificationService notificationService, 
            BlockService blockService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.notificationService = notificationService;
        this.blockService = blockService;
        this.messagingTemplate = messagingTemplate;
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

        var participants = chatParticipantRepository.findByRoomId(roomId);

        boolean isBlocked = participants.stream()
                .map(ChatParticipant::getUserId)
                .anyMatch(userId -> blockService.isBlocked(userId, senderId));

        if (isBlocked) {
            throw new ChatBlockedException(
                    "Message cannot be sent. You are blocked by a participant."
            );
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomId(roomId);
        chatMessage.setSenderId(senderId);
        chatMessage.setContent(message);
        chatMessage.setMessageType(MessageType.TEXT);

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        
        // Broadcast to WebSocket subscribers
        ChatMessageResponse response = mapToResponse(savedMessage);
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, response);

        participants.stream()
                .map(ChatParticipant::getUserId)
                .filter(userId -> !userId.equals(senderId))
                .filter(userId -> !blockService.isBlocked(userId, senderId))
                .forEach(userId ->
                        notificationService.createNotification(
                                userId,
                                "CHAT",
                                "New message received"
                        )
                );
    }

    @Override
    public List<ChatMessageResponse> getRoomMessages(UUID roomId, UUID userId) {

        boolean isParticipant = chatParticipantRepository
                .existsByRoomIdAndUserId(roomId, userId);

        if (!isParticipant) {
            throw new IllegalStateException("User is not a participant of this chat room");
        }

        return chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(roomId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ChatMessageResponse mapToResponse(ChatMessage message) {
        ChatMessageResponse response = new ChatMessageResponse();
        response.setId(message.getId());
        response.setSenderId(message.getSenderId());
        response.setContent(message.getContent());
        response.setMessageType(message.getMessageType().name());
        response.setCreatedAt(message.getCreatedAt());
        return response;
    }




}
