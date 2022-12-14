package com.example.nongdam.api.weather.controller;

import com.example.nongdam.api.weather.dto.response.WeatherResponse;
import com.example.nongdam.global.security.MemberDetail;
import com.example.nongdam.entity.weather.service.GeoService;
import com.example.nongdam.entity.weather.service.OpenWeatherApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/weather")
public class WeatherController {

    private final OpenWeatherApiService openWeatherApiService;
    private final GeoService geoService;

    @GetMapping("")

    public WeatherResponse getWeather(@AuthenticationPrincipal MemberDetail memberdetail) throws Exception {
        return openWeatherApiService.getWeather(memberdetail,memberdetail.getMember().getId());
    }

    @GetMapping("/geo")

    public String[] getGeo() throws Exception {
        return geoService.parseGeo("서울시 강서구 화곡로 320");
    }
}
