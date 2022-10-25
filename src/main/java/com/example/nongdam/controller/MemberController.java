package com.example.nongdam.controller;

import com.example.nongdam.dto.request.JoinMemberRequestDto;
import com.example.nongdam.dto.request.LoginRequestDto;
import com.example.nongdam.dto.response.JwtResponseDto;
import com.example.nongdam.dto.response.MemberInfoResponseDto;
import com.example.nongdam.security.MemberDetail;
import com.example.nongdam.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("")
    public void joinMember(@RequestBody JoinMemberRequestDto dto) {
        memberService.saveMember(dto);
    }

    // 로그인 정보 조회
    @GetMapping("")
    public MemberInfoResponseDto getMember(@AuthenticationPrincipal MemberDetail memberDetail) {

        return memberService.getMemberInfo(memberDetail.getMember());
    }

    // 로그인
    @PostMapping("/login")
    public String loginMember(@RequestBody LoginRequestDto dto, HttpServletResponse response) {
        System.out.println(dto.getEmail());
        JwtResponseDto token = memberService.login(dto, response);
        return "Bearer " + token.getToken();
    }
}
