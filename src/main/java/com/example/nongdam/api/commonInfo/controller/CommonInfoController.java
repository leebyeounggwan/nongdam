package com.example.nongdam.api.commonInfo.controller;

import com.example.nongdam.api.CropDto;
import com.example.nongdam.api.commonInfo.dto.response.PersonalCropDto;
import com.example.nongdam.global.security.MemberDetail;
import com.example.nongdam.entity.crop.service.CropService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommonInfoController {
    private final CropService cropService;

    @GetMapping("/crops")
    public List<CropDto> getAllCrops() {
        return cropService.findAllData();
    }

    @GetMapping("/crop")
    public List<PersonalCropDto> getPersonalCrop(@AuthenticationPrincipal MemberDetail memberdetail) {
        return cropService.getPersonalCrop(memberdetail);
    }

}
