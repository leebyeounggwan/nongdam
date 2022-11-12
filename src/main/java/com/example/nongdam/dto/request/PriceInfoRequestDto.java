package com.example.nongdam.dto.request;

import com.example.nongdam.entity.Crop;
import com.example.nongdam.security.MemberDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PriceInfoRequestDto {
    private String productClsCode;
    private String gradeRank;
    private String data;
    private int category;
    private int type;
    private String kind;
    private String name;
    private String countryCode;

    LocalDateTime dateTime = new LocalDateTime();

    private int year = dateTime.getYear();
    private int month = dateTime.getMonthOfYear();
    private int day = dateTime.getDayOfMonth();

    public PriceInfoRequestDto(Crop crop, String data, MemberDetail memberdetail) {

        this.productClsCode = null;
        this.gradeRank = null;
        this.category = crop.getCategory();
        this.type = crop.getType();
        this.kind = crop.getKind();
        this.name = crop.getName();
        this.countryCode = memberdetail.getMember().getCountryCode()+"";
        this.data = data;
    }
    public PriceInfoRequestDto(Crop crop, MemberDetail memberdetail, String clsCode) {
        this.productClsCode = clsCode;
        this.gradeRank = null;
        this.category = crop.getCategory();
        this.type = crop.getType();
        this.kind = crop.getKind();
        this.name = crop.getName();
        this.countryCode = memberdetail.getMember().getCountryCode()+"";
        this.data = null;
    }
}

