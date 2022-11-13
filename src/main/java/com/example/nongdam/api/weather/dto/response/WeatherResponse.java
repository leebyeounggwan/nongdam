package com.example.nongdam.api.weather.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WeatherResponse {
    // 기온

    private String temp;
    //강수량

    private String rn;
    //적설량
    private String sn;
    //풍속
    private String ws;
    //습도
    private String rhm;
    //날씨
    private String weather;
    //아이콘 URL
    private String iconURL;
    //지역
    private String address;
    //이슬점
    private String dewPoint;

    //시간별 날씨
    private WeatherDto hour;
    //주간 날씨
    private WeatherDto day;

    public WeatherResponse(CurrentTempDto currentTempDto, WeatherDto hourlyTemp, WeatherDto dailyTemp) {
        this.temp = currentTempDto.getTemp();
        this.rn = currentTempDto.getRn();
        this.sn = currentTempDto.getSn();
        this.ws = currentTempDto.getWs();
        this.rhm = currentTempDto.getRhm();
        this.weather = currentTempDto.getWeather();
        this.iconURL = currentTempDto.getIconURL();
        this.address = currentTempDto.getAddress();
        this.dewPoint = currentTempDto.getDewPoint();
        this.hour = hourlyTemp;
        this.day = dailyTemp;
    }
}

