package com.carecircle.communication.dto.response;

import java.util.UUID;

public class ChatRoomInitializationResponse {

    private UUID roomId;
    private boolean isNew;

    public ChatRoomInitializationResponse() {}

    public ChatRoomInitializationResponse(UUID roomId, boolean isNew) {
        this.roomId = roomId;
        this.isNew = isNew;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public boolean isIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
}
