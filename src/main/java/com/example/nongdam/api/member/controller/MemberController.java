package com.example.nongdam.api.member.controller;

import com.example.nongdam.api.member.dto.request.JoinMemberRequestDto;
import com.example.nongdam.api.member.dto.request.LoginRequestDto;
import com.example.nongdam.api.member.dto.request.MemberInfoRequestDto;
import com.example.nongdam.api.member.dto.response.JwtResponseDto;
import com.example.nongdam.api.member.dto.response.MemberInfoResponseDto;
import com.example.nongdam.global.exception.AuthenticationException;
import com.example.nongdam.global.security.MemberDetail;
import com.example.nongdam.entity.member.service.KakaoService;
import com.example.nongdam.entity.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {
    private final KakaoService ks;
    private final MemberService memberService;


    // 회원가입
    @PostMapping("")
    public void joinMember(@RequestBody JoinMemberRequestDto dto) {
        memberService.saveMember(dto);
    }

    // 로그인
    @PostMapping("/login")
    public String loginMember(@RequestBody LoginRequestDto dto, HttpServletResponse response) {
        JwtResponseDto token = memberService.login(dto, response);
        return "Bearer " + token.getToken();
    }

    //카카오로그인
    @PostMapping("/auth")
    public String accessTokenToMember(@RequestBody String t, HttpServletResponse response) throws AuthenticationException {

        JwtResponseDto token = ks.kakaoLogin(t, response);

        return token.getToken();
    }

    // 로그인 정보 조회
    @GetMapping("")
    public MemberInfoResponseDto getMember(@AuthenticationPrincipal MemberDetail memberDetail) throws AuthenticationException {
        if(memberDetail == null)
            throw new AuthenticationException("로그인이 필요한 서비스 입니다.","member info");

        return memberService.getMemberInfo(memberDetail.getMember());
    }

    @PutMapping("")
    public ResponseEntity<?> updateMember(@RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                          @RequestPart String data,
                                          @AuthenticationPrincipal MemberDetail memberDetails) throws JsonProcessingException, AuthenticationException {
        ObjectMapper mapper = new ObjectMapper();
        if(memberDetails == null)
            throw new AuthenticationException("로그인이 필요한 서비스 입니다.","update member");
        MemberInfoRequestDto requestDto = mapper.readValue(data, MemberInfoRequestDto.class);
        return memberService.updateMember(memberDetails.getMember().getProfileImage(), memberDetails.getMember().getId(), profileImage, requestDto, memberDetails.getMember().getEmail());
    }

}
