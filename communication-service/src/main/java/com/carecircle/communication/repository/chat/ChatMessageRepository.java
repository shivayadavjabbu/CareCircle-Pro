package com.carecircle.communication.repository.chat;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.carecircle.communication.model.chat.ChatMessage;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

	List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(UUID roomId);

	Page<ChatMessage> findByRoomIdOrderByCreatedAtDesc(UUID roomId, Pageable pageable);

	Optional<ChatMessage> findFirstByRoomIdOrderByCreatedAtDesc(UUID roomId);

	long countByRoomIdAndSenderIdNotAndReadFalse(UUID roomId, UUID senderId);
}
