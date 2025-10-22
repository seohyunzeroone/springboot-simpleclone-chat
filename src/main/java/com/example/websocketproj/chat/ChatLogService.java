// src/main/java/com/example/websocketproj/chat/ChatLogService.java
package com.example.websocketproj.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnBean(ChatLogRepository.class)  // 리포지토리가 생성된 경우에만 서비스 빈 등록
public class ChatLogService {
    private final ChatLogRepository repo;
    public void save(String username, String message) {
        repo.save(ChatLog.builder().username(username).message(message).build());
    }
}
