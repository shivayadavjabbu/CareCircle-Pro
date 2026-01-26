package com.carecircle.communication.dto.socket;

import java.util.UUID;

public class SendChatMessageSocketRequest {

    private UUID roomId;
    private String message;

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
