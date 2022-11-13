package com.example.nongdam.api.priceInfo.dto.response;

import com.example.nongdam.api.priceInfo.dto.request.PriceInfoRequestDto;
import com.example.nongdam.global.enums.CountryCode;
import com.example.nongdam.global.enums.CropTypeCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Getter
@NoArgsConstructor
public class PriceMetaDto {

    private String crop;
    private String type;
    private String unit;
    private String country;
    private String wholeSale;

    public PriceMetaDto(PriceInfoRequestDto priceInfoRequestDto, JSONObject parse_date, String data) {
        if (data.equals("year") || data.equals("month")) {
            String clsCode = (parse_date.get("productclscode").toString().equals("01")) ? "소매" : "도매";
            String[] captions = parse_date.get("caption").toString().split(" > ");
            String unit = getUnit(captions[5]);

            this.crop = captions[2];
            this.type = priceInfoRequestDto.getName();
            this.unit = unit;
            this.country = CountryCode.findByCountryCode(Integer.parseInt(priceInfoRequestDto.getCountryCode())).toString();
            this.wholeSale = clsCode;
        } else if (data.equals("day")){
            JSONObject parse_data = (JSONObject) parse_date.get("data");
            JSONArray parse_item = (JSONArray) parse_data.get("item");
            JSONObject parse_unit = (JSONObject) parse_item.get(parse_item.size()-1);
            String unit = getUnit(parse_unit.get("kindname").toString().split("\\(")[1].replaceAll("\\)", ""));

            this.crop = CropTypeCode.findByCode(priceInfoRequestDto.getType()).toString();
            this.type = priceInfoRequestDto.getName();
            this.unit = unit;
            this.country = CountryCode.findByCountryCode(Integer.parseInt(priceInfoRequestDto.getCountryCode())).toString();
            this.wholeSale = priceInfoRequestDto.getProductClsCode();
        }
    }

    public PriceMetaDto(PriceInfoRequestDto priceInfoRequestDto) {
        this.crop = CropTypeCode.findByCode(priceInfoRequestDto.getType()).toString();
        this.type = priceInfoRequestDto.getName();
        this.unit = "kg";
        this.country = CountryCode.findByCountryCode(Integer.parseInt(priceInfoRequestDto.getCountryCode())).toString();
        this.wholeSale = priceInfoRequestDto.getProductClsCode();
    }

    // 단위 추출
    private String getUnit(String captions) {
        String unit = captions;
        if (unit.replaceAll("[^\\d]", "").equals("1")) {
            unit = unit.substring(1);
        }
        return unit;
    }
}
