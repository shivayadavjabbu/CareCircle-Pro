package com.carecircle.communication.controller.chat;

import com.carecircle.communication.dto.request.AddParticipantRequest;
import com.carecircle.communication.dto.request.CreateChatRoomRequest;
import com.carecircle.communication.dto.request.SendMessageRequest;
import com.carecircle.communication.dto.response.ChatMessageResponse;
import com.carecircle.communication.dto.response.ChatRoomSummaryResponse;
import com.carecircle.communication.service.interfaces.ChatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/rooms")
    public ResponseEntity<Map<String, UUID>> createRoom(
            @RequestBody CreateChatRoomRequest request
    ) {
        UUID roomId = chatService.createChatRoom(request.getBookingId());
        return ResponseEntity.ok(Map.of("roomId", roomId));
    }

    @PostMapping("/rooms/{roomId}/participants")
    public ResponseEntity<Void> addParticipant(
            @PathVariable UUID roomId,
            @RequestBody AddParticipantRequest request
    ) {
        chatService.addParticipant(roomId, request.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rooms/{roomId}/messages")
    public ResponseEntity<Void> sendMessage(
            @PathVariable UUID roomId,
            @RequestHeader("X-User-Id") UUID senderId,
            @RequestBody SendMessageRequest request
    ) {
        chatService.sendMessage(roomId, senderId, request.getMessage());
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<Page<ChatMessageResponse>> getRoomMessages(
            @PathVariable UUID roomId,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                chatService.getRoomMessages(roomId, userId, pageable)
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<ChatRoomSummaryResponse>> getMyChatRooms(
            @RequestHeader("X-User-Id") UUID userId
    ) {
        return ResponseEntity.ok(chatService.getMyChatRooms(userId));
    }

    @PutMapping("/rooms/{roomId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable UUID roomId,
            @RequestHeader("X-User-Id") UUID userId
    ) {
        chatService.markAsRead(roomId, userId);
        return ResponseEntity.ok().build();
    }
}
