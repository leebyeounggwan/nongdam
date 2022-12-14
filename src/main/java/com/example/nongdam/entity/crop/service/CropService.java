package com.example.nongdam.entity.crop.service;

import com.example.nongdam.api.CropDto;
import com.example.nongdam.api.commonInfo.dto.response.PersonalCropDto;
import com.example.nongdam.entity.crop.entity.Crop;
import com.example.nongdam.entity.crop.repository.CropRepository;
import com.example.nongdam.entity.member.repository.MemberRepository;
import com.example.nongdam.global.security.MemberDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CropService {
    private final CropRepository cropRepository;
    private final MemberRepository memberRepository;

    public List<CropDto> findAllData(){
        List<Crop> crops = cropRepository.findAll();
        List<CropDto> ret = new ArrayList<>();
        crops.stream().forEach(e->ret.add(new CropDto(e)));
        return ret;
    }
    public List<PersonalCropDto> getPersonalCrop(MemberDetail memberdetail) {
        List<Crop> cropList = memberRepository.findAllByMember(memberdetail.getMember().getId());
        List<PersonalCropDto> list = new ArrayList<>();
        for (Crop c : cropList) {
            PersonalCropDto personalCropDto = new PersonalCropDto();
            personalCropDto.setId(c.getId());
            personalCropDto.setName(c.getName());
            list.add(personalCropDto);
        }
        return list;
    }
}