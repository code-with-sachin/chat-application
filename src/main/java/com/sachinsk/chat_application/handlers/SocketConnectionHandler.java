package com.sachinsk.chat_application.handlers;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SocketConnectionHandler extends TextWebSocketHandler {

    List<WebSocketSession> webSocketSessions =
            Collections.synchronizedList(new ArrayList<>());

   // Connecting the user to session
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println(session.getId() + " Connected");
        webSocketSessions.add(session);
    }

    //To remove user from Socket
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.out.println(session.getId() + " Disconnected");
        webSocketSessions.remove(session);
    }

    //To send and receive message real-time without refresh
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
        for (WebSocketSession webSocketSession : webSocketSessions) {
            //If current session = webSocketSession -> Then we don't send the message
            //i.e. If I am sending the message, then I should not get the messages. Instead, others should get the message(I will send the message to others chat)
            if(session == webSocketSession)
                continue;
            webSocketSession.sendMessage(message);
        }
    }
}
