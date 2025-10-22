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

    // DB/JPA가 켜진 환경에서만 주입되어 사용할 수 있도록 ObjectProvider 사용
    private final ObjectProvider<ChatLogService> chatLogServiceProvider;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload: {}", payload);

        // keepalive 무시
        if ("__ping__".equals(payload) || "__pong__".equals(payload)) return;

        // 모든 세션에 브로드캐스트
        for (WebSocketSession s : sessions) s.sendMessage(message);

        // DB 저장: "닉네임: 내용" 파싱
        ChatLogService svc = chatLogServiceProvider.getIfAvailable();
        if (svc != null) {
            String username = "anonymous";
            String body = payload;

            int idx = payload.indexOf(':');
            if (idx > 0) {
                username = payload.substring(0, idx).trim();
                body = payload.substring(idx + 1).trim();
            }
            svc.save(username, body);
        }
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("{} 접속", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("{} 접속 해제", session.getId());
    }
}
