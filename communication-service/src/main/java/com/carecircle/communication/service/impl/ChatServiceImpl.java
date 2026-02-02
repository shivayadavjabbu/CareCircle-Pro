package com.carecircle.communication.service.impl;

import com.carecircle.communication.dto.response.ChatMessageResponse;
import com.carecircle.communication.dto.response.ChatRoomInitializationResponse;
import com.carecircle.communication.dto.response.ChatRoomSummaryResponse;
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
import com.carecircle.communication.service.BookingIntegrationService;
import com.carecircle.communication.service.UserIntegrationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final UserIntegrationService userIntegrationService;
    private final BookingIntegrationService bookingIntegrationService; // Added

    public ChatServiceImpl(
            ChatRoomRepository chatRoomRepository,
            ChatParticipantRepository chatParticipantRepository,
            ChatMessageRepository chatMessageRepository, 
            NotificationService notificationService, 
            BlockService blockService,
            SimpMessagingTemplate messagingTemplate,
            UserIntegrationService userIntegrationService,
            BookingIntegrationService bookingIntegrationService
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.notificationService = notificationService;
        this.blockService = blockService;
        this.messagingTemplate = messagingTemplate;
        this.userIntegrationService = userIntegrationService;
        this.bookingIntegrationService = bookingIntegrationService;
    }

    @Override
    public ChatRoomInitializationResponse initializeChatRoom(UUID bookingId, UUID initiatorId, String initiatorRole, UUID partnerId) {
        
        // Case 1: Booking ID provided
        if (bookingId != null) {
            // Validate Booking
            var booking = bookingIntegrationService.getBooking(bookingId);
            if (booking == null) {
                 throw new IllegalArgumentException("Invalid Booking ID");
            }
            if (!"ACCEPTED".equalsIgnoreCase(booking.status()) && 
                !"COMPLETED".equalsIgnoreCase(booking.status())) { // Maybe allow completed too? REQ says "booking confirmed".
                 throw new IllegalStateException("Chat only allowed for CONFIRMED bookings.");
            }
            
            // Validate Participants match booking
            // Initiator must be Parent or Caregiver of the booking
            boolean isParent = booking.parentId().equals(initiatorId);
            boolean isCaregiver = booking.caregiverId().equals(initiatorId);
            
            if (!isParent && !isCaregiver) {
                 // Check if initiator is ADMIN? Creating room on behalf?
                 // Let's check initiator role via UserIntegration if valid
            }
            // For now enforce strict matching for non-admin flow
            
            return chatRoomRepository.findByBookingId(bookingId)
                .map(room -> new ChatRoomInitializationResponse(room.getId(), false))
                .orElseGet(() -> {
                    ChatRoom room = new ChatRoom();
                    room.setType(ChatRoomType.DIRECT);
                    room.setBookingId(bookingId);
                    ChatRoom savedRoom = chatRoomRepository.save(room);

                    addParticipant(savedRoom.getId(), booking.parentId());
                    addParticipant(savedRoom.getId(), booking.caregiverId());

                    return new ChatRoomInitializationResponse(savedRoom.getId(), true);
                });
        }
        
        // Case 2: No Booking ID (Admin <-> User context)
        if (bookingId == null) {
             if (initiatorRole == null || !initiatorRole.contains("ADMIN")) {
                 throw new IllegalStateException("Only Admins can initiate chat without a booking.");
             }
             
             // Admin Logic: Check for existing room
             List<UUID> commonRoomIds = chatParticipantRepository.findCommonRoomIds(initiatorId, partnerId);
             
             // Find first DIRECT room
             for (UUID roomId : commonRoomIds) {
                 ChatRoom room = chatRoomRepository.findById(roomId).orElse(null);
                 if (room != null && room.getType() == ChatRoomType.DIRECT && room.getBookingId() == null) {
                      // Found existing direct chat
                      return new ChatRoomInitializationResponse(roomId, false);
                 }
             }
             
             // Create new if not found
             ChatRoom room = new ChatRoom();
             room.setType(ChatRoomType.DIRECT);
             room.setBookingId(null); // Admin chat has no booking
             ChatRoom savedRoom = chatRoomRepository.save(room);
             
             addParticipant(savedRoom.getId(), initiatorId);
             addParticipant(savedRoom.getId(), partnerId);
             
             return new ChatRoomInitializationResponse(savedRoom.getId(), true);
        }
        
        return null; // Should not reach
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
            throw new ChatBlockedException("Message cannot be sent. You are blocked by a participant.");
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomId(roomId);
        chatMessage.setSenderId(senderId);
        chatMessage.setContent(message);
        chatMessage.setMessageType(MessageType.TEXT);

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        
        // Broadcast
        ChatMessageResponse response = mapToResponse(savedMessage);
        userIntegrationService.getUsersInfo(List.of(senderId)).values().stream()
                .findFirst()
                .ifPresent(u -> response.setSenderName(formatName(u))); // Use formatted name

        messagingTemplate.convertAndSend("/topic/chat/" + roomId, response);

        participants.stream()
                .map(ChatParticipant::getUserId)
                .filter(userId -> !userId.equals(senderId))
                .filter(userId -> !blockService.isBlocked(userId, senderId))
                .forEach(userId ->
                        notificationService.createNotification(userId, "CHAT", "New message received")
                );
    }

    @Override
    public List<ChatMessageResponse> getRoomMessages(UUID roomId, UUID userId) {
        boolean isParticipant = chatParticipantRepository.existsByRoomIdAndUserId(roomId, userId);
        if (!isParticipant) throw new IllegalStateException("User is not a participant of this chat room");

        List<ChatMessage> messages = chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
        List<UUID> senderIds = messages.stream().map(ChatMessage::getSenderId).distinct().toList();
        var userMap = userIntegrationService.getUsersInfo(senderIds);

        return messages.stream()
                .map(m -> {
                    ChatMessageResponse res = mapToResponse(m);
                    if (userMap.containsKey(m.getSenderId())) {
                        res.setSenderName(formatName(userMap.get(m.getSenderId())));
                    }
                    return res;
                })
                .toList();
    }

    @Override
    public Page<ChatMessageResponse> getRoomMessages(UUID roomId, UUID userId, Pageable pageable) {
        boolean isParticipant = chatParticipantRepository.existsByRoomIdAndUserId(roomId, userId);
        if (!isParticipant) throw new IllegalStateException("User is not a participant of this chat room");

        Page<ChatMessage> messagePage = chatMessageRepository.findByRoomIdOrderByCreatedAtDesc(roomId, pageable);
        List<UUID> senderIds = messagePage.getContent().stream().map(ChatMessage::getSenderId).distinct().toList();
        var userMap = userIntegrationService.getUsersInfo(senderIds);

        return messagePage.map(m -> {
            ChatMessageResponse res = mapToResponse(m);
            if (userMap.containsKey(m.getSenderId())) {
                res.setSenderName(formatName(userMap.get(m.getSenderId())));
            }
            return res;
        });
    }

    @Override
    public List<ChatRoomSummaryResponse> getMyChatRooms(UUID userId) {
        List<ChatParticipant> myParticipations = chatParticipantRepository.findByUserId(userId);
        
        // Fetch all generic room details (Type, BookingId)
        List<UUID> roomIds = myParticipations.stream().map(ChatParticipant::getRoomId).toList();
        java.util.Map<UUID, ChatRoom> roomMap = chatRoomRepository.findAllById(roomIds).stream()
                .collect(java.util.stream.Collectors.toMap(ChatRoom::getId, java.util.function.Function.identity()));

        List<ChatRoomSummaryResponse> summaries = myParticipations.stream().map(p -> {
            UUID roomId = p.getRoomId();
            ChatRoomSummaryResponse summary = new ChatRoomSummaryResponse();
            summary.setRoomId(roomId);

            List<ChatParticipant> participants = chatParticipantRepository.findByRoomId(roomId);
            UUID partnerId = participants.stream()
                    .map(ChatParticipant::getUserId)
                    .filter(id -> !id.equals(userId))
                    .findFirst()
                    .orElse(null);
            
            summary.setPartnerId(partnerId);

            chatMessageRepository.findFirstByRoomIdOrderByCreatedAtDesc(roomId)
                    .ifPresent(m -> {
                        summary.setLastMessage(m.getContent());
                        summary.setLastMessageTime(m.getCreatedAt());
                    });

            long unread = chatMessageRepository.countByRoomIdAndSenderIdNotAndReadFalse(roomId, userId);
            summary.setUnreadCount(unread);
            
            summary.setPartnerName("Unknown User");

            return summary;
        }).toList();

        List<UUID> partnerIds = summaries.stream()
                .map(ChatRoomSummaryResponse::getPartnerId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();

        if (!partnerIds.isEmpty()) {
            var userMap = userIntegrationService.getUsersInfo(partnerIds);
            summaries.forEach(s -> {
                if (s.getPartnerId() != null && userMap.containsKey(s.getPartnerId())) {
                    String baseName = formatName(userMap.get(s.getPartnerId()));
                    
                    // Add Context (Booking vs Direct)
                    ChatRoom room = roomMap.get(s.getRoomId());
                    if (room != null && room.getBookingId() != null) {
                         String shortId = room.getBookingId().toString().substring(0, 4);
                         baseName += " (Booking #" + shortId + ")";
                    }
                    
                    s.setPartnerName(baseName);
                }
            });
        }
        
        // Deduplicate: If multiple rooms result in exact same content, distinct() can help if equals() is implemented, 
        // but here objects are new. 
        // We rely on distinct roomIds. 
        // However, if myParticipations has duplicates (same user/room multiple times), use distinct on room IDs stream above.
        // The implementation above maps myParticipations (List) -> summaries (List).
        // If 'myParticipations' has duplicates, 'summaries' has duplicates.
        // We should distinct 'myParticipations' by RoomID first.
        
        return summaries.stream()
                .filter(distinctByKey(ChatRoomSummaryResponse::getRoomId)) // Ensure unique rooms
                .toList();
    }
    
    // Utility for distinct stream
    private static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
        java.util.Set<Object> seen = java.util.concurrent.ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Override
    public void markAsRead(UUID roomId, UUID userId) {
        List<ChatMessage> unreadMessages = chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(roomId).stream()
                .filter(m -> !m.getSenderId().equals(userId) && !m.isRead())
                .toList();
        
        unreadMessages.forEach(m -> m.setRead(true));
        chatMessageRepository.saveAll(unreadMessages);
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
    
    // Helper to format name with Role Prefix
    private String formatName(UserIntegrationService.UserSummary user) {
        if (user == null) return "Unknown";
        String prefix = "";
        String role = user.userRole() != null ? user.userRole().toUpperCase() : "";
        
        if (role.contains("ADMIN")) {
            prefix = "RA_";
        } else if (role.contains("PARENT")) {
            prefix = "RP_";
        } else if (role.contains("CAREGIVER") || role.contains("CARETAKER")) {
            prefix = "RC_";
        }
        
        return prefix + user.fullName();
    }
}
