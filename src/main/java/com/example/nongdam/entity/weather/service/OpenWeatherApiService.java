package com.example.nongdam.entity.weather.service;

import com.example.nongdam.api.weather.dto.response.CurrentTempDto;
import com.example.nongdam.api.weather.dto.response.WeatherDto;
import com.example.nongdam.api.weather.dto.response.WeatherResponse;
import com.example.nongdam.global.security.MemberDetail;
import com.example.nongdam.entity.OpenApiService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@Service
@RequiredArgsConstructor
public class OpenWeatherApiService {
    private final OpenApiService openApiService;
    private final GeoService geoService;
    @Value("${weather.api.url}")
    private String baseURL;

    @Value("${weather.api.key}")
    private String key;

    //@UseCache(ttl = 0L,cacheKey = "cacheKey",unit = TimeUnit.MINUTES,timeData = false)
    public WeatherResponse getWeather(MemberDetail memberdetail, int cacheKey) throws Exception {

        String address = (!memberdetail.getMember().getAddress().isEmpty()) ? memberdetail.getMember().getAddress() : "서울시 강서구 화곡로 320";
        String[] coords = geoService.parseGeo(address);

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("lat",coords[1]);
        params.add("lon",coords[0]);
        params.add("appid",key);
        params.add("units","metric");
        params.add("lang","kr");


        String result = openApiService.callAPI(params, baseURL);
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(result);
        System.out.println("obj = " + obj);

        // 현재 기온
        CurrentTempDto currentTempDto = currentTempParse(obj, address);
        // 시간별 기온
        WeatherDto hourlyTemp = setTempList(obj, "hourly");
        // 주간 기온
        WeatherDto dailyTemp = setTempList(obj, "daily");

        return new WeatherResponse(currentTempDto, hourlyTemp, dailyTemp);
    }


    public CurrentTempDto currentTempParse(JSONObject obj, String address) {
        
        return new CurrentTempDto(obj, address);
    }

    public WeatherDto setTempList (JSONObject obj, String select) {

        return new WeatherDto(obj, select);
    }

}
