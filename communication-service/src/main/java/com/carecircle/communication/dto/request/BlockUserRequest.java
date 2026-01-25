package com.carecircle.communication.dto.request;

import java.util.UUID;

public class BlockUserRequest {

    private UUID userId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
