package com.example.nongdam.dto.response;


import com.example.nongdam.enums.WeatherDescription;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Getter
@NoArgsConstructor
public class CurrentTempDto {

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

    public CurrentTempDto(JSONObject obj, String address) {
        JSONObject parse_response = (JSONObject) obj.get("current");
        JSONObject snow = (JSONObject) parse_response.get("snow");
        JSONObject rain = (JSONObject) parse_response.get("rain");
        JSONArray parse_weather = (JSONArray) parse_response.get("weather");
        JSONObject value = (JSONObject) parse_weather.get(0);
        String icon = value.get("icon").toString();
        String[] strAddr = address.split(" ");

        this.temp = parse_response.get("temp").toString().split("\\.")[0];
        this.rn = (rain == null) ? "0" : rain.get("1h").toString();
        this.sn = (snow == null) ? "0" : snow.get("1h").toString();
        this.ws = String.format("%.1f", Double.parseDouble(parse_response.get("wind_speed").toString()));
        this.rhm = parse_response.get("humidity").toString();
        this.weather = WeatherDescription.findByNumber(Integer.parseInt(value.get("id").toString())).label();
        this.iconURL = "http://idontcare.shop/static/weathericon/"+icon+".png";
        this.address = strAddr[0]+" "+strAddr[1];
        this.dewPoint = String.format("%.1f", Double.parseDouble(parse_response.get("dew_point").toString()));
    }
}
