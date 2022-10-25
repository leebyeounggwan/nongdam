package com.example.nongdam.service;

//import com.example.nongdam.security.JwtProvider;
//import com.example.nongdam.security.KakaoOauth;
import com.example.nongdam.security.OAuthAttributes;
import com.example.nongdam.entity.Member;
import com.example.nongdam.exception.AuthenticationException;
import com.example.nongdam.repository.MemberRepository;
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
//    private final JwtProvider provider;

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
    public String kakaoLogin(String code, HttpServletResponse response) throws AuthenticationException, AuthenticationException {
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
        System.out.println(attr.getName());
        System.out.println(attr.getEmail());
        System.out.println(attr.getId());
        if(m == null){
            m = Member.builder()
                    .nickname(attr.getName())
                    .email(attr.getEmail())
                    .build();
        }
        System.out.println(m.getEmail());
        System.out.println(m.getName());
        System.out.println(m.getId());
//        m.updateMember(attr);
        memberRepository.save(m);
        return m.toString();
    }


}


    /*public String getToken(String code) throws IOException {
        // 인가코드로 토큰받기
        String host = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String token = "";
        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=2aad40910868e3c5fa9594f8de34a07b");
            sb.append("&redirect_uri=http://localhost:8080/member/kakao");
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("result = " + result);

            // json parsing
            org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
            JSONObject elem = (JSONObject) parser.parse(result);

            String access_token = elem.get("access_token").toString();
            String refresh_token = elem.get("refresh_token").toString();
            System.out.println("refresh_token = " + refresh_token);
            System.out.println("access_token = " + access_token);

            token = access_token;

            br.close();
            bw.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }


        return token;
    }


    public Map<String, Object> getUserInfo(String access_token) throws IOException {
        String host = "https://kapi.kakao.com/v2/user/me";
        Map<String, Object> result = new HashMap<>();
        try {
            URL url = new URL(host);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + access_token);
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);


            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String res = "";
            while((line=br.readLine())!=null)
            {
                res+=line;
            }

            System.out.println("res = " + res);


            org.json.simple.parser.JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(res);
            JSONObject kakao_account = (JSONObject) obj.get("kakao_account");
            JSONObject properties = (JSONObject) obj.get("properties");


            String id = obj.get("id").toString();
            String nickname = properties.get("nickname").toString();
            String age_range = kakao_account.get("age_range").toString();

            result.put("id", id);
            result.put("nickname", nickname);
            result.put("age_range", age_range);

            br.close();


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getAgreementInfo(String access_token)
    {
        String result = "";
        String host = "https://kapi.kakao.com/v2/user/scopes";
        try{
            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer "+access_token);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            while((line=br.readLine())!=null)
            {
                result+=line;
            }

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            // result is json format
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }*/