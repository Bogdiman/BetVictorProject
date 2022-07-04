package com.betvictor.actionmonitor.validator;

import com.betvictor.actionmonitor.dto.MessageDTO;
import org.springframework.stereotype.Component;

@Component
public class MessageValidator {

    public void validateMessageHasSender(MessageDTO msg) throws Exception {
        if (msg.getSenderUsername().isEmpty()) {
            throw new Exception("Msg sender cannot be empty!");
        }
    }
}
