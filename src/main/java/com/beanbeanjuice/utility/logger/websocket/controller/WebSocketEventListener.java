package com.beanbeanjuice.utility.logger.websocket.controller;

import com.beanbeanjuice.CafeBot;
import com.beanbeanjuice.utility.logger.LogLevel;
import com.beanbeanjuice.utility.logger.websocket.model.ChatMessage;
import com.beanbeanjuice.utility.logger.websocket.model.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    @Autowired
    private SimpMessageSendingOperations sendingOperations;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        CafeBot.getLogManager().log(this.getClass(), LogLevel.INFO, "User Connected to Websocket!");
        CafeBot.getLogManager().setSendingOperations(sendingOperations);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        ChatMessage chatMessage = ChatMessage.builder()
                .type(MessageType.DISCONNECT)
                .sender(username)
                .build();

        CafeBot.getLogManager().log(this.getClass(), LogLevel.INFO, "User Disconnected from Websocket: " + username);
        sendingOperations.convertAndSend("/topic/public", chatMessage);
    }

}
