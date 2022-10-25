package com.example.nongdam.service;

import com.example.nongdam.dto.request.JoinMemberRequestDto;
import com.example.nongdam.dto.request.LoginRequestDto;
import com.example.nongdam.dto.response.JwtResponseDto;
import com.example.nongdam.dto.response.MemberInfoResponseDto;
import com.example.nongdam.entity.Member;
import com.example.nongdam.repository.MemberRepository;
import com.example.nongdam.repository.TokenRepository;
import com.example.nongdam.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final BCryptPasswordEncoder encoder;
    private final MemberRepository memberRepository;
    private final JwtProvider provider;
    private final TokenRepository tokenRepository;


    public void checkEmail (String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }

    public void saveMember(JoinMemberRequestDto dto) {
        checkEmail(dto.getEmail());
        memberRepository.save(dto.build(encoder));
    }

    public JwtResponseDto login(LoginRequestDto dto, HttpServletResponse response) {
        System.out.println(dto.getEmail());
        Member member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
        if (encoder.matches(dto.getPassword(), member.getPassword())) {
            return provider.generateToken(member, response);
        } else throw new IllegalArgumentException("계정 혹은 비밀번호가 다릅니다.");
    }

    public MemberInfoResponseDto getMemberInfo(Member member) {
        return new MemberInfoResponseDto(member);
    }

}
