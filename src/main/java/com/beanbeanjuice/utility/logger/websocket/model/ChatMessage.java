package com.beanbeanjuice.utility.logger.websocket.model;

import lombok.Builder;
import lombok.Getter;

@Builder
public class ChatMessage {

    @Getter
    private String content;

    @Getter
    private String sender;

    @Getter
    private String time;

    @Getter
    private MessageType type;

}
