package com.tgnewsbot.dto;

import lombok.Data;

import java.util.List;

@Data
public class NewsResponse {
    private String status;
    private Integer totalResults;
    private List<Article> articles;
}
