package com.tgnewsbot.service;

import com.tgnewsbot.dto.NewsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateConsumerService implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient tgClient;
    private final ForeignNewsService newsService;

    @Override
    public void consume(Update update) {
        var inputMsg = update.getMessage();
        SendMessage sendMsg;
        if (inputMsg.getText().equals("/start")) {
            sendMsg = SendMessage.builder()
                    .chatId(inputMsg.getChatId())
                    .text("Hello, you can find new by example: get bitcoin")
                    .build();
        } else {
            sendMsg = requestNews(inputMsg);
        }

        try {
            tgClient.execute(sendMsg);
        } catch (TelegramApiException e) {
            log.error("Error send message in tg: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private SendMessage requestNews(Message inputMsg) {
        SendMessage sendMsg;
        var values = inputMsg.getText().split(" ");
        var query = values[values.length - 1];
        String outputMsg;
        try {
            var newsResponse = newsService.getNews(query);
            outputMsg = getNewsTitle(newsResponse, query);
        } catch (Exception e) {
            log.error(e.getMessage());
            outputMsg = "Error found new by " + query;
        }
        sendMsg = SendMessage.builder()
                .chatId(inputMsg.getChatId())
                .text(outputMsg.isEmpty() ? "Not found news by query: " + query : outputMsg)
                .build();
        return sendMsg;
    }

    private String getNewsTitle(NewsResponse newsResponse, String query) {
        String outputMsg = "";
        if (newsResponse != null && Objects.equals(newsResponse.getStatus(), "ok")) {
            if (newsResponse.getArticles().isEmpty()) {
                outputMsg = "Not found news by query: " + query;
            } else {
                var article = newsResponse.getArticles().getFirst();
                StringBuilder builder = new StringBuilder();
                builder.append("Last new: ");
                builder.append("\n");
                builder.append("Author: ");
                builder.append(article.getAuthor());
                builder.append("\n");
                builder.append("Title: ");
                builder.append(article.getTitle());
                builder.append(article.getContent());
                outputMsg = builder.toString();
            }
        }
        return outputMsg;
    }
}
