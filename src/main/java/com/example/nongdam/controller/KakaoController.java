package com.example.nongdam.controller;

import com.example.nongdam.exception.AuthenticationException;
import com.example.nongdam.security.MemberDetail;
import com.example.nongdam.security.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.nongdam.service.KakaoService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoController {
    private final String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpZCIsImlkIjoxMDUsImV4cCI6MTY2NjQ1ODgzNCwiaWF0IjoxNjY2NDU1MjM0fQ.tRueGwj9S_3F-_WoPwMWYEBqoWf_oLJ-aCSaW7gyKFA";
    private final String refreshToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NjY0NTUyMzQsImV4cCI6MTY2NjU0MTYzNH0.jzA3xa82deCOsDc5lWVJ1Dg9c8dZ0yI9VIad_MuuCFA";

    private final KakaoService ks;


    @PostMapping("/member/auth")
    public String accessTokenToMember(@RequestBody String t, HttpServletResponse response) throws AuthenticationException {
        System.out.println("call api@@@@@@@@@@@@@@@@@@@@@");

        String s = ks.kakaoLogin(t, response);
        System.out.println("String s = ks.kakaoLogin(t, response) = " + s);
//        response.addHeader("Authorization", token);
//        response.addHeader("RefreshToken", refreshToken);

        return s;
    }

    @GetMapping("/test")//@AuthenticationPrincipal MemberDetail memberDetail
    public String tokenTest() {
//        System.out.println(memberDetail.getMember().getEmail());
//        return memberDetail.getMember().getEmail();
        return "ok";
    }

}
