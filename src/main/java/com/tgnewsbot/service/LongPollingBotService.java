package com.tgnewsbot.service;

import com.tgnewsbot.config.BotConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

@Service
@RequiredArgsConstructor
public class LongPollingBotService implements SpringLongPollingBot {
    private final BotConfig botConfig;
    private final UpdateConsumerService updateConsumerService;

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateConsumerService;
    }
}
