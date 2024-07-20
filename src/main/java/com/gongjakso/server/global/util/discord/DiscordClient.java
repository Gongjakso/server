package com.gongjakso.server.global.util.discord;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DiscordClient {

    @Value("${discord.environment}")
    private String environment;

    @Value("${discord.webhook-url}")
    private String webhookUrl;

    public void sendErrorMessage(Integer code, String message, String stackTrace) {
        if(!environment.equals("prod")) {
            return;
        }

        WebClient webClient = WebClient.create();

        //요청 본문
        Map<String, Object> embedData = new HashMap<>();

        embedData.put("title", "공작소 서버 에러 발생");

        Map<String, String> field1 = new HashMap<>();
        field1.put("name", "발생시각");
        field1.put("value", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));

        Map<String, String> field2 = new HashMap<>();
        field2.put("name", "에러 코드");
        field2.put("value", code.toString());

        Map<String, String> field3 = new HashMap<>();
        field3.put("name", "에러 명");
        field3.put("value", message);

        Map<String, String> field4 = new HashMap<>();
        field4.put("name", "스택 트레이스");
        field4.put("value", stackTrace);

        embedData.put("fields", List.of(field1, field2, field3, field4));

        Map<String, Object> payload = new HashMap<>();
        payload.put("embeds", new Object[]{embedData});

        webClient.post()
                .uri(webhookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
