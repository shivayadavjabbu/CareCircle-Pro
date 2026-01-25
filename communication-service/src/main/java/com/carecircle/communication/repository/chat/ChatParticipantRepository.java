package com.carecircle.communication.repository.chat;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carecircle.communication.model.chat.ChatParticipant;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, UUID> {
}