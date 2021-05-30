package com.beanbeanjuice.main.websocket.controller;

import com.beanbeanjuice.main.CafeBot;
import com.beanbeanjuice.main.websocket.model.ChatMessage;
import com.beanbeanjuice.main.websocket.model.MessageType;
import com.beanbeanjuice.utility.logger.LogLevel;
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
        CafeBot.getLogManager().log(this.getClass(), LogLevel.INFO, "A user has connected to the websocket server.");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        CafeBot.getLogManager().log(this.getClass(), LogLevel.INFO, "A user has disconnected.");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        ChatMessage chatMessage = ChatMessage.builder()
                .type(MessageType.DISCONNECT)
                .sender(username)
                .build();

        sendingOperations.convertAndSend("/topic/public", chatMessage);
    }

}
