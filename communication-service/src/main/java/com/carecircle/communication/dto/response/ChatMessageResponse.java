package com.carecircle.communication.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChatMessageResponse {

    private UUID id;
    private UUID senderId;
    private String content;
    private String messageType;
    private LocalDateTime createdAt;
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public UUID getSenderId() {
		return senderId;
	}
	public void setSenderId(UUID senderId) {
		this.senderId = senderId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

    
}
