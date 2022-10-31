package com.example.nongdam.controller;

import com.example.nongdam.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping ("/news")
    public void getNews() {

    }
}
