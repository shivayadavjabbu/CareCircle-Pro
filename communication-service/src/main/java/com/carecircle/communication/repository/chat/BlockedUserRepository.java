package com.carecircle.communication.repository.chat;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carecircle.communication.model.chat.BlockedUser;

public interface BlockedUserRepository extends JpaRepository<BlockedUser, UUID> {
}
