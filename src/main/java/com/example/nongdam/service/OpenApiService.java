package com.example.nongdam.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class OpenApiService {

    public String callAPI(MultiValueMap<String, String> params, String baseURL) {

        Mono<String> jsonObjectMono = WebClient.builder().baseUrl(baseURL)
                .build().post()
                .uri(builder -> builder.queryParams(params).build())
                .exchangeToMono(response -> response.bodyToMono(String.class));
        return jsonObjectMono.block();
    }
}
