package com.betvictor.actionmonitor.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private int messageId;
    private Timestamp messageTimestamp;
    private String senderUsername;
    private String message;
}
