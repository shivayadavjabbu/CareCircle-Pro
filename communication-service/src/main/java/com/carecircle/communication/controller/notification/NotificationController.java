package com.carecircle.communication.controller.notification;

import com.carecircle.communication.model.notification.Notification;
import com.carecircle.communication.service.interfaces.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications(
            @RequestHeader("X-User-Id") UUID userId
    ) {
        return ResponseEntity.ok(
                notificationService.getUserNotifications(userId)
        );
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable UUID notificationId,
            @RequestHeader("X-User-Id") UUID userId
    ) {
        notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.ok().build();
    }
}
