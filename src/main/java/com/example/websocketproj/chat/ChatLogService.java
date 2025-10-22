package com.example.websocketproj.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatLogService {

    // 레포지토리가 있으면 사용, 없으면 스킵
    private final ObjectProvider<ChatLogRepository> repoProvider;

    public void save(String username, String message) {
        ChatLogRepository repo = repoProvider.getIfAvailable();
        if (repo == null) {
            log.warn("[ChatLogService] ChatLogRepository bean not available. Skip persist.");
            return;
        }
        log.info("[ChatLogService] save username={}, message={}", username, message);
        repo.save(ChatLog.builder()
                .username(username)
                .message(message)
                .build());
    }
}
