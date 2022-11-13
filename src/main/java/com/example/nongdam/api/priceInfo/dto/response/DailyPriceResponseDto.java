package com.example.nongdam.api.priceInfo.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


@Getter
@NoArgsConstructor
public class DailyPriceResponseDto {
    private String crop;
    private String type;
    private String unit;
    private String country;
    private String wholeSale;
    private String latestDate;
    private String latestDatePrice;

    public DailyPriceResponseDto(PriceMetaDto meta, JSONObject obj) {

        JSONObject parse_data = (JSONObject) obj.get("data");
        JSONArray parse_item = (JSONArray) parse_data.get("item");
        JSONObject parse_latestDate = new JSONObject();

        parse_latestDate = getLatestPrice(parse_item, parse_latestDate);

        this.crop = meta.getCrop();
        this.type = meta.getType();
        this.unit = meta.getUnit();
        this.country = meta.getCountry();
        this.wholeSale = meta.getWholeSale();
        this.latestDate = parse_latestDate.get("yyyy").toString() + "-" + parse_latestDate.get("regday").toString().replace("/", "-");
        this.latestDatePrice = parse_latestDate.get("price").toString();
    }

    public DailyPriceResponseDto(PriceMetaDto meta) {
        this.crop = meta.getCrop();
        this.type = meta.getType();
        this.unit = meta.getUnit();
        this.country = meta.getCountry();
        this.wholeSale = meta.getWholeSale();
        this.latestDate = "";
        this.latestDatePrice = "";
    }

    private JSONObject getLatestPrice(JSONArray parse_item, JSONObject parse_latestDate) {
        for (int i = 0; i < parse_item.size(); i++) {
            parse_latestDate = (JSONObject) parse_item.get(i);
            if (parse_latestDate.get("countyname").equals("평년")) {
                parse_latestDate = (JSONObject) parse_item.get(i-1);
                break;
            }
        }
        return parse_latestDate;
    }
}

