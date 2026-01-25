package com.carecircle.communication.service.impl;

import com.carecircle.communication.model.chat.BlockedUser;
import com.carecircle.communication.repository.chat.BlockedUserRepository;
import com.carecircle.communication.service.interfaces.BlockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BlockServiceImpl implements BlockService {

    private final BlockedUserRepository blockedUserRepository;

    public BlockServiceImpl(BlockedUserRepository blockedUserRepository) {
        this.blockedUserRepository = blockedUserRepository;
    }

    @Override
    public void blockUser(UUID blockerId, UUID blockedId) {
        if (blockedUserRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId)) {
            return; // idempotent
        }

        BlockedUser blockedUser = new BlockedUser();
        blockedUser.setBlockerId(blockerId);
        blockedUser.setBlockedId(blockedId);

        blockedUserRepository.save(blockedUser);
    }

    @Override
    public void unblockUser(UUID blockerId, UUID blockedId) {
        blockedUserRepository.findAll().stream()
                .filter(b ->
                        b.getBlockerId().equals(blockerId)
                        && b.getBlockedId().equals(blockedId)
                )
                .forEach(blockedUserRepository::delete);
    }

    @Override
    public boolean isBlocked(UUID blockerId, UUID blockedId) {
        return blockedUserRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId);
    }
    
    @Override
    public List<UUID> getBlockedUsers(UUID blockerId) {
        return blockedUserRepository.findByBlockerId(blockerId).stream()
                .map(BlockedUser::getBlockedId)
                .toList();
    }

}
