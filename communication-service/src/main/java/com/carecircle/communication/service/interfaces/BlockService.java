package com.carecircle.communication.service.interfaces;

import java.util.List;
import java.util.UUID;

public interface BlockService {

    void blockUser(UUID blockerId, UUID blockedId);

    void unblockUser(UUID blockerId, UUID blockedId);

    boolean isBlocked(UUID blockerId, UUID blockedId);
    
    List<UUID> getBlockedUsers(UUID blockerId);
}
