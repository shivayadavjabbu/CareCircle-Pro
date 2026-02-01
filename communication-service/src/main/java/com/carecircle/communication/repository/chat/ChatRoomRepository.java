package com.carecircle.communication.repository.chat;

import com.carecircle.communication.model.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
    Optional<ChatRoom> findByBookingId(UUID bookingId);
}
