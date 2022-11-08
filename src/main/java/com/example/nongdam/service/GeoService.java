package com.example.nongdam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;


import java.io.UnsupportedEncodingException;

import java.nio.charset.StandardCharsets;

//8034983E-14EF-31F7-B6BA-CAC219E38FD7
@Service
@Slf4j
@RequiredArgsConstructor
public class GeoService {
    private final OpenApiService openApiService;
    @Value("${geocoder.api.url}")
    private String baseURL;

    @Value("${geocoder.api.key}")
    private String key;

    public String getGeo(String address) {

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("service","address");
        params.add("request","getcoord");
        params.add("version","2.0");
        params.add("crs","epsg:4326");
        params.add("address",address);
        params.add("refine","true");
        params.add("simple","false");
        params.add("format","json");
        params.add("type","road");
        params.add("key",key);

        return openApiService.callAPI(params, baseURL);
    }

    public String[] parseGeo (String address) throws ParseException {
        String result = getGeo(address);

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(result);
        JSONObject parse_response = (JSONObject) obj.get("response");
        JSONObject parse_result = (JSONObject) parse_response.get("result");
        JSONObject parse_point = (JSONObject) parse_result.get("point");
        String x = parse_point.get("x").toString();
        String y = parse_point.get("y").toString();

        return new String[] {x,y};

    }


    }


