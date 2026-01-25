package com.carecircle.communication.repository.chat;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carecircle.communication.model.chat.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

}
