package com.example.websocketproj.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ChatController {

    // /chat 요청 시 templates/chater.html 반환
    @GetMapping("/chat")
    public String chatGET() {
        log.info("@ChatController, chat GET()");
        return "chater"; // src/main/resources/templates/chater.html
    }
}
