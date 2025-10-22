package com.example.websocketproj.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnBean(ChatLogRepository.class) // 로컬(JPA off)에서도 안전
public class ChatLogService {
    private final ChatLogRepository repo;

    public void save(String username, String message) {
        log.info("[ChatLogService] save username={}, message={}", username, message);
        repo.save(ChatLog.builder()
                .username(username)
                .message(message)
                // .createdAt(LocalDateTime.now())   // 서비스에서 넣는 방식 선택 시 주석 해제
                .build());
    }
}
