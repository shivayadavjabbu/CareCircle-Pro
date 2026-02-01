package com.carecircle.communication.service.interfaces;

import java.util.List;
import java.util.UUID;

import com.carecircle.communication.dto.response.ChatMessageResponse;
import com.carecircle.communication.dto.response.ChatRoomSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatService {

    UUID createChatRoom(UUID bookingId);

    void addParticipant(UUID roomId, UUID userId);

    void sendMessage(UUID roomId, UUID senderId, String message);
    
    // Legacy support or specific use case
    List<ChatMessageResponse> getRoomMessages(UUID roomId, UUID userId);

    Page<ChatMessageResponse> getRoomMessages(UUID roomId, UUID userId, Pageable pageable);

    List<ChatRoomSummaryResponse> getMyChatRooms(UUID userId);

    void markAsRead(UUID roomId, UUID userId);
}
