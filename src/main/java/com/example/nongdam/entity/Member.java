package com.example.nongdam.entity;


import com.example.nongdam.dto.request.JoinMemberRequestDto;
import com.example.nongdam.security.OAuthAttributes;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    @Builder.Default
    private String address = "";

    @Column
    private int countryCode;

    @Column
    private String profileImage;
    @Column
    private String nickname;

    @Column(columnDefinition = "boolean default true")
    @Builder.Default
    private boolean isLock = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @Builder.Default
    private List<Crop> crops = new ArrayList<>();

    public Member(JoinMemberRequestDto dto) {
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.nickname = dto.getNickname();
    }

    public void updateMember(OAuthAttributes attributes) {
        this.name = attributes.getName();
        this.profileImage = attributes.getPicture();
    }

//    public Member build(BCryptPasswordEncoder encoder, JoinMemberRequestDto dto) {
//        return Member.builder()
//                .name(dto.getName())
//                .email(dto.getEmail())
//                .password(dto.getPassword())
//                .nickname(dto.getNickname())
//                .build();
//    }



//    public void changePassword(String password){
//        this.password = password;
//    }
//    public void updateMember(OAuthAttributes attributes) {
//        this.name = attributes.getName();
//        this.profileImage = attributes.getPicture();
//    }
//
//    public void updateMember(MemberInfoRequestDto requestDto, Map<String, String> profileImage, CropRepository repository) {
//        this.nickname = requestDto.getNickname() == null ? nickname : requestDto.getNickname();
//        this.address = requestDto.getAddress() == null ? address : requestDto.getAddress();
//        this.countryCode = requestDto.getCountryCode() == 0 ? countryCode : requestDto.getCountryCode();
//        this.profileImage = profileImage.get("url");
//        this.crops.clear();
//        List<Crop> cr = repository.findAllIds(requestDto.getCrops());
//        this.crops.addAll(cr);
//    }
//
//    public void updateMember(MemberInfoRequestDto requestDto, String defaultImage, CropRepository repository) {
//        // 프로필 사진 없이 업데이트
//        this.nickname = requestDto.getNickname() == null ? nickname : requestDto.getNickname();
//        this.address = requestDto.getAddress() == null ? address : requestDto.getAddress();
//        this.countryCode = requestDto.getCountryCode() == 0 ? countryCode : requestDto.getCountryCode();
//        this.profileImage = defaultImage; //기존 이미지
//        this.crops.clear();
//        List<Crop> cr = repository.findAllIds(requestDto.getCrops());
//        this.crops.addAll(cr);
//    }
//
//    public void enableId() {
//        if (isLock)
//            this.isLock = false;
//    }
}