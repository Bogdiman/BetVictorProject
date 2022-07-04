package com.betvictor.actionmonitor.service;

import com.betvictor.actionmonitor.dto.MessageDTO;
import com.betvictor.actionmonitor.model.Message;
import com.betvictor.actionmonitor.repository.MessageRepository;
import com.betvictor.actionmonitor.validator.MessageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageValidator validator;

    public Message postMessage(MessageDTO msg) throws Exception {
        validator.validateMessageHasSender(msg);

        return messageRepository.save(Message.builder()
                .messageId(msg.getMessageId())
                .senderUsername(msg.getSenderUsername())
                .messageTimestamp(msg.getMessageTimestamp())
                .message(msg.getMessage())
                .build());
    }

}
