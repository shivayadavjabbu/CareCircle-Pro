package com.carecircle.communication.service.interfaces;

import com.carecircle.communication.dto.response.ChatMessageResponse;
import com.carecircle.communication.dto.response.ChatRoomInitializationResponse;
import com.carecircle.communication.dto.response.ChatRoomSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ChatService {

    ChatRoomInitializationResponse initializeChatRoom(UUID bookingId, UUID initiatorId, UUID partnerId);

    void addParticipant(UUID roomId, UUID userId);

    void sendMessage(UUID roomId, UUID senderId, String message);
    
    // Legacy support or specific use case
    List<ChatMessageResponse> getRoomMessages(UUID roomId, UUID userId);

    Page<ChatMessageResponse> getRoomMessages(UUID roomId, UUID userId, Pageable pageable);

    List<ChatRoomSummaryResponse> getMyChatRooms(UUID userId);

    void markAsRead(UUID roomId, UUID userId);
}
