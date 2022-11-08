package com.example.nongdam.service;

import com.example.nongdam.annotation.UseCache;
import com.example.nongdam.dto.response.NewsResponseDto;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class NewsService {

    @Value("${news.naver.api.url}")
    private String apiUrl;

    @Value("${news.naver.api.client-id}")
    private String clientId;

    @Value("${news.naver.api.client-secret}")
    private String clientSecret;

    private String getNewsData(String query, int count){
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id",clientId);
        headers.set("X-Naver-Client-Secret",clientSecret);
        headers.set("Content-Type","application/json");
        HttpEntity entity = new HttpEntity(headers);
        String url = apiUrl+"?query="+query+"&display="+count;
        ResponseEntity<String> response = template.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
        String object = response.getBody();
        return object;
    }
    @UseCache(cacheKey = "tmp", ttl = 30L,unit= TimeUnit.MINUTES,timeData = false)
    public List<NewsResponseDto> getNewsInfo(String tmp) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(getNewsData("농사",15));
        JSONArray newsData = (JSONArray) object.get("items");
        List<NewsResponseDto> ret = new ArrayList<>();
        int i = 0;
        while(ret.size() != 10){
            JSONObject o = (JSONObject) newsData.get(i);
            try{
                ret.add(new NewsResponseDto(o));
            }catch (IOException|IndexOutOfBoundsException e){

            }
            i++;
            if(i >= newsData.size())
                break;
        }
        return ret;
    }

}
