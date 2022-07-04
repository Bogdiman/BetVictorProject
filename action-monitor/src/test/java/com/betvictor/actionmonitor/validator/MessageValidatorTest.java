package com.betvictor.actionmonitor.validator;

import com.betvictor.actionmonitor.dto.MessageDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageValidatorTest {

    @Autowired
    private MessageValidator validator;

    @Test
    public void testMessageValidator_should_throwException_when_usernameIsEmpty() {
        assertThrows(Exception.class, () -> validator.validateMessageHasSender(MessageDTO.builder().senderUsername("").build()));
    }
}
