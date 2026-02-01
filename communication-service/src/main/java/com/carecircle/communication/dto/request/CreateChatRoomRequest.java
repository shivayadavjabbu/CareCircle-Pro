package com.carecircle.communication.dto.request;

import java.util.UUID;

public class CreateChatRoomRequest {

    private UUID bookingId;
    private UUID partnerId;

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public UUID getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(UUID partnerId) {
        this.partnerId = partnerId;
    }
}
