package com.example.nongdam.service;

import com.example.nongdam.security.OAuthAttributes;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAPIService {

    private final RestTemplate template;

    public MultiValueMap<String, String> setParameters(Map<String, String> values) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.setAll(values);
        return params;
    }

    public HttpEntity setHeaders(MediaType type, Map<String, String> values) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        for (String key : values.keySet()) {
            headers.set(key, values.get(key));
        }
        return new HttpEntity(headers);
    }

    public HttpEntity getResult (MultiValueMap<?,?> params, HttpEntity httpEntity, String url, HttpMethod httpMethod) {

        ResponseEntity<Map> res = template.exchange(url, httpMethod, httpEntity, Map.class, params);

        return res;
    }


//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.set("Authorization", "Bearer "+accessToken);
//    org.springframework.http.HttpEntity request = new org.springframework.http.HttpEntity(headers);
//    public HttpEntity setHeader(List<Map> values) {
//        HttpHeaders headers = new HttpHeaders();
//
//        for (Map value : values) {
//             headers.set(value.get(), value.)
//        }
//
//
//        ResponseEntity<Map> values = new ResponseEntity<>(null);
//        return null;
//    }
    // 헤더 넣는 method
    // 파라미터로 헤더에 넣을 값을 map으로 받고 size 만큼 add or set

    // 헤더값, 쿼리파라미터 값들을 map으로 만드는 메서드

    // 레디스 생성 시 파라미터로 위에 두개 메서드를 넣어고 body를 리턴 Object로
}
