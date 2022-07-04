package com.betvictor.actionmonitor.validator;

import com.betvictor.actionmonitor.dto.MessageDTO;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MessageValidator {

    public void validateMessage(MessageDTO msg) throws Exception {
        validateMessageHasSender(msg);
        validateMessageHasBody(msg);
    }

    private void validateMessageHasSender(MessageDTO msg) throws Exception {
        if (StringUtils.isBlank(msg.getSenderUsername())) {
            throw new Exception("Message sender cannot be empty!");
        }
    }

    private void validateMessageHasBody(MessageDTO msg) throws Exception {
        if (StringUtils.isBlank(msg.getMessage())) {
            throw new Exception("Message cannot be empty!");
        }
    }
}
