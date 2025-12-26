package com.tgnewsbot.service;

import com.tgnewsbot.dto.NewsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ForeignNewsService {
    private final RestTemplate restTemplate;
    @Value("${feign.news.key}")
    private String newsKey;

    public NewsResponse getNews(String name) {
        var uri = UriComponentsBuilder.fromUriString("https://newsapi.org/v2/everything")
                .queryParam("q", name)
                .queryParam("from", LocalDate.now().minusDays(1L).toString())
                .queryParam("sortBy", "popularity")
                .queryParam("apiKey", newsKey)
                .queryParam("pageSize", 1)
                .build()
                .toUri();

        return restTemplate.getForObject(uri, NewsResponse.class);
    }
}
