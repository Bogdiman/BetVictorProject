package com.betvictor.actionmonitor.controller;

import com.betvictor.actionmonitor.dto.MessageDTO;
import com.betvictor.actionmonitor.model.Message;
import com.betvictor.actionmonitor.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActivityMonitorControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private MessageRepository messageRepository;

    private WebSocketStompClient webSocketStompClient;

    @BeforeEach
    public void setup() {
        this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));
    }

    @Test
    public void testSuccessfulMessage() throws Exception {
        BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(getWsPath(), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe("/topic/messages", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add(payload);
            }
        });

        session.send("/app/chat", MessageDTO.builder().message("Hello!").senderUsername("Bogdan").messageTimestamp(Timestamp.from(Instant.now())).build());

        Message responseMessage = (Message) Objects.requireNonNull(blockingQueue.poll(5, SECONDS));

        assertEquals("Hello!", responseMessage.getMessage());
        assertEquals("Bogdan", responseMessage.getSenderUsername());

        assertEquals(1, messageRepository.findAll().size());
    }

    @Test
    public void testFailedMessage() throws Exception {
        BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect(getWsPath(), new StompSessionHandlerAdapter() {})
                .get(1, SECONDS);

        session.subscribe("/topic/messages", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Object.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add(payload);
            }
        });

        session.send("/app/chat", MessageDTO.builder().message("Hello!").senderUsername("").messageTimestamp(Timestamp.from(Instant.now())).build());

        Object response = blockingQueue.poll(5, SECONDS);
        //There should be no response on /topic/messages
        assertNull(response);
    }

    private String getWsPath() {
        return String.format("ws://localhost:%d/chat", port);
    }
}
