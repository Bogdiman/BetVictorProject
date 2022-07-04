package com.betvictor.actionmonitor.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int messageId;

    @Column
    private Timestamp messageTimestamp;

    @Column
    private String senderUsername;

    @Column
    private String message;
}
