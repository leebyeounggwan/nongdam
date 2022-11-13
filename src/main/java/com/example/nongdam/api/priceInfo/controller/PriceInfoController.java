package com.example.nongdam.api.priceInfo.controller;

import com.example.nongdam.api.priceInfo.dto.request.PriceInfoRequestDto;
import com.example.nongdam.api.priceInfo.dto.response.DailyPriceResponseDto;
import com.example.nongdam.api.priceInfo.dto.response.PriceInfoDto;
import com.example.nongdam.entity.crop.entity.Crop;
import com.example.nongdam.entity.crop.repository.CropRepository;
import com.example.nongdam.global.security.MemberDetail;
import com.example.nongdam.entity.priceInfo.service.PriceInfoService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class PriceInfoController {
    private final PriceInfoService priceInfoService;
    private final CropRepository cropRepository;


    @GetMapping("/todaymarketprice/{cropId}/{productClsCode}")
    public DailyPriceResponseDto todayPriceInfo(@PathVariable("cropId") int cropId, @PathVariable("productClsCode")String productClsCode,
                                                @AuthenticationPrincipal MemberDetail memberdetail) throws ParseException {
        Crop crop = cropRepository.findById(cropId).orElseThrow(() -> new NullPointerException("해당 작물이 없습니다."));
        CacheKey cacheKey = new CacheKey(memberdetail, productClsCode, crop);
        PriceInfoRequestDto priceInfoRequestDto = new PriceInfoRequestDto(crop, memberdetail, productClsCode);

        return priceInfoService.dailyPrice(priceInfoRequestDto, cacheKey.key);
    }

    @GetMapping("/marketprice")
    public List<PriceInfoDto> priceInfo(@RequestParam int cropId, @RequestParam String data,
                                        @AuthenticationPrincipal MemberDetail memberdetail) throws ParseException {
        Crop crop = cropRepository.findById(cropId).orElseThrow(() -> new NullPointerException("해당 작물이 없습니다."));
        CacheKey cacheKey = new CacheKey(memberdetail, data, crop);
        PriceInfoRequestDto priceInfoRequestDto = new PriceInfoRequestDto(crop, data, memberdetail);

        return priceInfoService.monthAndYearPrice(priceInfoRequestDto, cacheKey.key);
    }

    @GetMapping("/marketprices/{data}")
    public List<List<PriceInfoDto>> myPriceInfoMonth(
            @PathVariable String data,
            @AuthenticationPrincipal MemberDetail memberdetail) throws ParseException {

        List<List<PriceInfoDto>> responseDtoList = new ArrayList<>();
        List<Crop> crops = memberdetail.getMember().getCrops();

        if (crops.size() != 0) {
            for (Crop crop : crops) {
                CacheKey cacheKey = new CacheKey(memberdetail, data, crop);
                PriceInfoRequestDto priceInfoRequestDto = new PriceInfoRequestDto(crop, data, memberdetail);
                List<PriceInfoDto> reponseDto = priceInfoService.monthAndYearPrice(priceInfoRequestDto, cacheKey.key);

                responseDtoList.add(reponseDto);
            }
        }
        return responseDtoList;
    }

    private static class CacheKey {
        String type;
        String country;
        String countryCode;
        int cropId;
        String key;

        private CacheKey(MemberDetail memberdetail, String data, Crop crop) {
            this.type = data;
            this.country = memberdetail.getMember().getCountryCode()+"";
            this.countryCode = (country.equals("0")) ? "1101" : country;
            this.cropId = crop.getId();
            this.key = cropId + countryCode + type;
        }
    }

}
