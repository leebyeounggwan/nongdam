package com.example.nongdam.dto.request;

import com.example.nongdam.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinMemberRequestDto {
    //이메일, 비번, 이름, 닉네임

    private String name;

    private String email;

    private String password;

    private String nickname;

//    public JoinMemberRequestDto(String name, String email, String password, String nickname) {
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.nickname = nickname;
//    }

    public Member build(BCryptPasswordEncoder encoder) {
        return  Member.builder()
                    .name(name)
                    .email(email)
                    .password(encoder.encode(password))
                    .nickname(nickname)
                    .build();
    }

}
