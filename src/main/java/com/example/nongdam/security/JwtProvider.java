package com.example.nongdam.security;


import com.example.nongdam.dto.response.JwtResponseDto;
import com.example.nongdam.entity.Member;
import com.example.nongdam.entity.RefreshToken;
import com.example.nongdam.repository.MemberRepository;
import com.example.nongdam.repository.TokenRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private final String JWT_SECRET = Base64.getEncoder().encodeToString("NongDam".getBytes());
    //    private final long ValidTime = 1000L * 30;
    private final long ValidTime = 1000L * 60 * 60;
    private final long refreshValidTime = 1000L * 60 * 60 * 24;
    private final MemberRepository memberRepository;

    private final TokenRepository tokenRepository;

//    private QueryDslRepository queryDsl;
//
//    @Autowired
//    private RedisTemplate<String, Object> template;

//    @Autowired
//    public JwtProvider(QueryDslRepository queryDsl) {this.queryDsl = queryDsl;}

    public void setTokenHeader(JwtResponseDto token, HttpServletResponse response) {
        response.addHeader("Authorization",token.getToken());
        response.addHeader("RefreshToken",token.getRefreshToken());
    }
    public JwtResponseDto generateToken(Member m,HttpServletResponse response) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", m.getId());
//        claims.put("member",m);
        String token = doGenerateToken(claims, "id");
        String refreshToken = doGenerateRefreshToken(token);

        JwtResponseDto jwtResponseDto = new JwtResponseDto(token, refreshToken);
        setTokenHeader(jwtResponseDto,response);

        return jwtResponseDto;
    }

    public String generateToken(Member m) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", m.getId());
//        claims.put("member",m);

        return doGenerateToken(claims, "id");
    }
//    public String generateToken(String s){
//        Map<String,Object> claims = new HashMap<>();
//        claims.put("key",s);
//        return doGenerateToken(claims,"key");
//    }
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ValidTime))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }

    private String doGenerateRefreshToken(String jwtToken) {

        String refreshToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshValidTime))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
        saveRefreshToken(jwtToken, refreshToken);
        return refreshToken;
    }
    // refresh token 저장
    public void saveRefreshToken(String jwtToken, String refreshToken){
        refreshToken = refreshToken.replaceAll("Bearer ","");
        jwtToken = jwtToken.replaceAll("Bearer ","");

        RefreshToken saveToken = new RefreshToken(jwtToken, refreshToken);

        tokenRepository.setToken(saveToken);
    }


    // Principal 반환
    public Authentication getAuthentication(MemberDetail userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    public boolean checkRefreshToken(String refreshToken,String jwtToken){

        RefreshToken byToken = tokenRepository.getToken(jwtToken);
        return byToken != null || byToken.getRefreshToken().equals(refreshToken);
    }

    public MemberDetail getMemberDetail(String token) {
        int id = Integer.parseInt(getUserPk(token));
        Member member = getMember(id);
        return new MemberDetail(member);
    }
    //@UseCache(cacheKey = "id", ttl = 2L,unit = TimeUnit.HOURS,timeData = true)
    public Member getMember(int id){

        return memberRepository.findById(id).orElseThrow(null);
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        try {
            return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().get("id").toString();
        }catch (ExpiredJwtException e){
            return e.getClaims().get("id").toString();
        }
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public void setAuthHeader(HttpServletResponse response, String token) {
        response.setHeader("Authorization", "Bearer " + token);
    }

    //리프레시 토큰 검사 후 토큰 재발급
//    @Transactional
//    public String checkRefreshToken(String token, String reFreshToken) {
////        RefreshToken byReFreshToken = refreshTokenRepository.findByMemberId(1);
//        RefreshToken byReFreshToken = refreshTokenRepository.findByRefreshToken(reFreshToken);
//        if(byReFreshToken.getRefreshToken().equals(reFreshToken) && byReFreshToken.getJwtToken().equals(token)) {
//            Member member = repo.findById(byReFreshToken.getId()).orElseThrow(null);
//            Map<String, Object> claims = new HashMap<>();
//            claims.put("id", member.getId());
//
//            String newToken = doGenerateToken(claims, "id");
//            byReFreshToken.setJwtToken(newToken);
//            return newToken;
//        }
//        return null;
//    }
}

