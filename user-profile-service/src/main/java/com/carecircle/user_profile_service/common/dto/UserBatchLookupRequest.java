package com.carecircle.user_profile_service.common.dto;

import java.util.List;
import java.util.UUID;

public class UserBatchLookupRequest {
    private List<UUID> userIds;

    public UserBatchLookupRequest() {}

    public UserBatchLookupRequest(List<UUID> userIds) {
        this.userIds = userIds;
    }

    public List<UUID> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<UUID> userIds) {
        this.userIds = userIds;
    }
}
