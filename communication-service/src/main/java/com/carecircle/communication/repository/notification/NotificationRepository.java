package com.carecircle.communication.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carecircle.communication.model.notification.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
	 List<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
