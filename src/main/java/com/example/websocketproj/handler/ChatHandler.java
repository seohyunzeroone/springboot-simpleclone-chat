// src/main/java/com/example/websocketproj/handler/ChatHandler.java
package com.example.websocketproj.handler;

import com.example.websocketproj.chat.ChatLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private static final List<WebSocketSession> sessions = new ArrayList<>();

    // 주입을 선택적으로 받기: 서비스가 없으면(null) 그냥 건너뜀
    private final ObjectProvider<ChatLogService> chatLogServiceProvider;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload: {}", payload);

        // DB 저장: 서비스가 로드된 환경에서만 실행
        ChatLogService svc = chatLogServiceProvider.getIfAvailable();
        if (svc != null) {
            svc.save("anonymous", payload);
        }

        // 브로드캐스트 기존 동작 유지
        for (WebSocketSession s : sessions) {
            s.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("{} 접속", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        log.info("{} 접속 해제", session.getId());
    }
}
