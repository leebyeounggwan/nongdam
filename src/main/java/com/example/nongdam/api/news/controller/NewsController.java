package com.example.nongdam.api.news.controller;

import com.example.nongdam.api.news.dto.response.NewsResponseDto;
import com.example.nongdam.entity.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping ("/news")
    public List<NewsResponseDto> getNews() throws IOException, ParseException, org.json.simple.parser.ParseException {
        List<NewsResponseDto> ret = newsService.getNewsInfo("");
        for (NewsResponseDto r : ret)
            r.setTime();
        return ret;
    }
}
