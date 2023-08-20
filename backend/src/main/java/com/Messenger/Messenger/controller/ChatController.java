package com.Messenger.Messenger.controller;

import com.Messenger.Messenger.entity.ChatMessage;
import com.Messenger.Messenger.entity.ChatNotification;
import com.Messenger.Messenger.service.ChatMessageService;
import com.Messenger.Messenger.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private ChatMessageService chatMessageService;
    @Autowired private ChatRoomService chatRoomService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage){
        var chatID = chatRoomService.getChatId
                (
                chatMessage.getSenderId(),
                chatMessage.getRecipientId(),
                true
                );
        chatMessage.setChatId(chatID.get());

        ChatMessage saved = chatMessageService.save(chatMessage);

        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId().toString(), "/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName()
                )
        );
    }

}
