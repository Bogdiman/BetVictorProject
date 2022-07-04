package com.betvictor.actionmonitor.controller;

import com.betvictor.actionmonitor.dto.MessageDTO;
import com.betvictor.actionmonitor.model.Message;
import com.betvictor.actionmonitor.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ActivityMonitorController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message send(MessageDTO message) throws Exception {
        return messageService.postMessage(message);
    }

    @MessageExceptionHandler
    @SendTo("/topic/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
