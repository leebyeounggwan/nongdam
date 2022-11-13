package com.example.nongdam.entity.member.service;

//import com.example.nongdam.security.JwtProvider;
//import com.example.nongdam.security.KakaoOauth;
import com.example.nongdam.api.member.dto.response.JwtResponseDto;
import com.example.nongdam.entity.member.entity.Member;
import com.example.nongdam.global.security.JwtProvider;
import com.example.nongdam.global.security.OAuthAttributes;
import com.example.nongdam.global.exception.AuthenticationException;
import com.example.nongdam.entity.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate template;
    private final MemberRepository memberRepository;
    private final JwtProvider provider;


    public String getAccessToken(String code){
        //
        MultiValueMap<String,Object> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
//        params.add("client_id",kakaoOauth.getKAKAO_CLIENT_ID());
        params.add("client_id","70c694cc264803900c72070ca488b4ed");
//        params.add("redirect_uri",kakaoOauth.getREDIRECT_URI());
        params.add("redirect_uri","http://localhost:3000/code/auth");
        params.add("code",code);
        ResponseEntity<Map> response = template.postForEntity("https://kauth.kakao.com/oauth/token",params,Map.class);
        if(response.getStatusCode() != HttpStatus.OK)
            throw new IllegalArgumentException("코드가 잘못되었습니다.");
        Map<String,Object> ret = response.getBody();
        return ret.get("access_token").toString();
    }

    @Transactional
    public JwtResponseDto kakaoLogin(String code, HttpServletResponse response) throws AuthenticationException {
        // 인가 코드로 액세스 토큰 요청
        String accessToken = getAccessToken(code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer "+accessToken);
        HttpEntity request = new HttpEntity(headers);

        //받아온 토큰으로 사용자 정보 받아옴
        ResponseEntity<Map> res = template.exchange("https://kapi.kakao.com/v2/user/me",HttpMethod.GET,request,Map.class);

        OAuthAttributes attr = OAuthAttributes.ofKakao(null,res.getBody());
        Member m = memberRepository.findByEmail(attr.getEmail()).orElse(null);

        if(m == null){
            m = Member.builder()
                    .nickname(attr.getName())
                    .email(attr.getEmail())
                    .build();
        }
        // ???? 이름이 널로 들어가서 에러남
        m.updateMember(attr);

        memberRepository.save(m);
        return provider.generateToken(m, response);
    }


}

