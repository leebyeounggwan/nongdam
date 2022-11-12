package com.example.nongdam.dto.response;

import com.example.nongdam.FinalValue;
import com.example.nongdam.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
@AllArgsConstructor
public class MemberInfoResponseDto {

    private int id;
    private String name;
    private String nickname;
    private String email;
    private String address;
    private String profileImage;
    private int countryCode;
    private List<CropDto> crops = new ArrayList<>();

    public MemberInfoResponseDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.address = member.getAddress();
        this.nickname = member.getNickname();
        this.profileImage = member.getProfileImage() == null ? FinalValue.BACK_URL + "/static/default.png" : member.getProfileImage();
        this.countryCode = member.getCountryCode();
        member.getCrops().forEach(e -> this.crops.add(new CropDto(e)));
    }
}
