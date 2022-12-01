package com.example.nongdam.entity.priceInfo.service;

import com.example.nongdam.api.priceInfo.dto.request.PriceApiRequestVariableDto;
import com.example.nongdam.api.priceInfo.dto.request.PriceInfoRequestDto;
import com.example.nongdam.api.priceInfo.dto.response.DailyPriceResponseDto;
import com.example.nongdam.api.priceInfo.dto.response.PriceInfoDto;
import com.example.nongdam.api.priceInfo.dto.response.PriceMetaDto;
import com.example.nongdam.entity.OpenApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class PriceInfoService {
    private final OpenApiService openApiService;
    @Value("${price.api.key}")
    private String apiKey;
    @Value("${price.api.certid}")
    private String certId;

    @Value("${price.api.url}")
    private String baseURL;

    //일별 시세
    //@UseCache(cacheKey = "cacheKey", ttl = -1L,unit = TimeUnit.HOURS,timeData = false)
    public DailyPriceResponseDto dailyPrice(PriceInfoRequestDto priceInfoRequestDto, String cacheKey) throws ParseException {
        String data = "day";
        List<String> dailyDate = makeDateList(data);
        PriceApiRequestVariableDto var = new PriceApiRequestVariableDto(priceInfoRequestDto, dailyDate);

        try {
            JSONObject obj = callPriceInfoAPI(data, var);
            if (obj.get("data").getClass().getSimpleName().equals("JSONObject")) {
                PriceMetaDto meta = new PriceMetaDto(priceInfoRequestDto, obj, data);
                return new DailyPriceResponseDto(meta, obj);
            } else {
                return new DailyPriceResponseDto(new PriceMetaDto(priceInfoRequestDto));
            }
        }
        catch (NullPointerException | ClassCastException | IndexOutOfBoundsException e) {
            return new DailyPriceResponseDto(new PriceMetaDto(priceInfoRequestDto));
        }
    }

    //월별/연도별 시세
    //@UseCache(cacheKey = "cacheKey", ttl = -1L,unit = TimeUnit.HOURS,timeData = false)
    public List<PriceInfoDto> monthAndYearPrice(PriceInfoRequestDto priceInfoRequestDto, String cacheKey) throws ParseException {
        String data = priceInfoRequestDto.getData();
        List<String> setDateList = makeDateList(data);
        Collections.reverse(setDateList);
        List<PriceInfoDto> priceList = new ArrayList<>();
        PriceApiRequestVariableDto var = new PriceApiRequestVariableDto(priceInfoRequestDto);


        try {
            JSONObject obj = callPriceInfoAPI(data, var);
            JSONArray parse_price = new JSONArray();
            parse_price = checkType(obj, parse_price);

            for (int i = 0; i < parse_price.size(); i++) {
                JSONObject parse_date = (JSONObject) parse_price.get(i);
                PriceMetaDto meta = new PriceMetaDto(priceInfoRequestDto, parse_date, data);
                PriceInfoDto priceInfoDto = new PriceInfoDto(priceInfoRequestDto, meta, setDateList, parse_date);
                priceList.add(priceInfoDto);
                if (parse_price.size() == 1) {
                    plusOne(meta, priceList, priceInfoDto);
                }
            }
        } catch (NullPointerException | ClassCastException | IndexOutOfBoundsException e) {
            for (int i = 0; i < 2; i++) {
                PriceMetaDto meta = new PriceMetaDto(priceInfoRequestDto);
                PriceInfoDto monthPriceInfoDto = new PriceInfoDto(meta, i);
                priceList.add(monthPriceInfoDto);
            }
        }
        return priceList;
    }

    // 도,소매 둘중에 하나만 있는 경우
    public void plusOne(PriceMetaDto meta, List<PriceInfoDto> priceList, PriceInfoDto priceInfoDto) {
        if (priceInfoDto.getWholeSale().equals("도매")) {
            priceList.add(new PriceInfoDto(meta, 1));
        } else if (priceInfoDto.getWholeSale().equals("소매")) {
            priceList.add(new PriceInfoDto(meta, 0));
            Collections.reverse(priceList);
        }
    }

    // 타입 체크
    public JSONArray checkType(JSONObject obj, JSONArray parse_price) {
        if (obj.get("price").getClass().getSimpleName().equals("JSONObject")) {
            parse_price.add(obj.get("price"));
        } else {
            parse_price = (JSONArray) obj.get("price");
        }
        return parse_price;
    }

    // 일별/월별/연도별 날짜 리스트 생성
    public List<String> makeDateList(String select) {
        List<String> dateList = new ArrayList<>();

        Date today = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        switch (select) {
            case "day":
                String endDay = sdf.format(today);
                cal.add(Calendar.MONTH, -1);
                String startDay = sdf.format(cal.getTime());
                dateList.add(startDay);
                dateList.add(endDay);
                break;
            case "month":
                cal.add(Calendar.MONTH, 0);
                for (int i = 0; i < 7; i++) {
                    String date = sdf.format(cal.getTime()).split("-")[0]+"."+sdf.format(cal.getTime()).split("-")[1];
                    cal.add(Calendar.MONTH, -2);
                    dateList.add(date);
                }
                break;
            case "year":
                cal.add(Calendar.YEAR, 0);
                for (int i = 0; i < 6; i++) {
                    String date = sdf.format(cal.getTime()).split("-")[0];
                    cal.add(Calendar.YEAR, -1);
                    dateList.add(date);
                }
                break;
        }
        return dateList;
    }

    public JSONObject callPriceInfoAPI(String select, PriceApiRequestVariableDto var) throws ParseException {

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();

        switch (select) {
            case "day":
                params.add("action", "periodProductList");
                params.add("p_productrankcode", var.getGradeRank());
                params.add("p_productclscode",var.getClsCode());
                params.add("p_startday",var.getStartDay());
                params.add("p_endday",var.getEndDay());
                break;
            case "month":
                params.add("action", "monthlySalesList");
                params.add("p_yyyy", var.getNowYear());
                params.add("p_period", "3");
                params.add("p_graderank", var.getGradeRank());
                break;
            case "year":
                params.add("action", "yearlySalesList");
                params.add("p_yyyy", var.getNowYear());
                params.add("p_graderank", var.getGradeRank());
                break;
        }
        params.add("p_itemcategorycode",var.getCategoryCode());
        params.add("p_itemcode",var.getItemCode());
        params.add("p_kindcode",var.getKindCode());
        params.add("p_countrycode",var.getCountryCode());
        params.add("p_convert_kg_yn","Y");
        params.add("p_cert_key",apiKey);
        params.add("p_cert_id",certId);
        params.add("p_returntype","json");

        String result;
        try {
            result = openApiService.callAPI(params, baseURL, "get");
            // data : 001 = no data
            JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(result);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
