package com.carecircle.communication.controller.chat;

import com.carecircle.communication.service.interfaces.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(required = false) UUID bookingId
    ) {
        UUID roomId = chatService.createChatRoom(bookingId);
        return ResponseEntity.ok(Map.of("roomId", roomId));
    }

    @PostMapping("/rooms/{roomId}/participants")
    public ResponseEntity<Void> addParticipant(
            @PathVariable UUID roomId,
            @RequestParam UUID userId
    ) {
        chatService.addParticipant(roomId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rooms/{roomId}/messages")
    public ResponseEntity<Void> sendMessage(
            @PathVariable UUID roomId,
            @RequestParam UUID senderId,
            @RequestParam String message
    ) {
        chatService.sendMessage(roomId, senderId, message);
        return ResponseEntity.ok().build();
    }
}
