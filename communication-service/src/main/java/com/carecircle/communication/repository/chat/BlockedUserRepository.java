package com.carecircle.communication.repository.chat;

import com.carecircle.communication.model.chat.BlockedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BlockedUserRepository extends JpaRepository<BlockedUser, UUID> {

    boolean existsByBlockerIdAndBlockedId(UUID blockerId, UUID blockedId);

    List<BlockedUser> findByBlockerId(UUID blockerId);
}

