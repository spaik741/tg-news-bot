package com.tgnewsbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class ClientConfig {

    @Bean
    public TelegramClient tgClient(BotConfig botConfig) {
        return new OkHttpTelegramClient(botConfig.getToken());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
