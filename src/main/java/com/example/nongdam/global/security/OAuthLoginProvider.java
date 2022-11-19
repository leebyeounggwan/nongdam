package com.example.nongdam.global.security;

import com.example.nongdam.entity.member.entity.Member;
import com.example.nongdam.entity.member.repository.MemberRepository;
import com.example.nongdam.global.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collections;

@RequiredArgsConstructor
@Component
public class OAuthLoginProvider implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository repository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User user = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributes attr = null;
        try {
            attr = OAuthAttributes.of(registrationId, userNameAttributeName, user.getAttributes());
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        saveOrUpdate(attr);
        return new DefaultOAuth2User(Collections.emptyList(), attr.getAttributes(), userNameAttributeName);
    }

    // DB 유무 확인
    private void saveOrUpdate(OAuthAttributes attributes) {
        Member member = repository.findByEmail(attributes.getEmail()).orElse(null);
        if (member == null)
            member = Member.builder().email(attributes.getEmail()).name(attributes.getName()).profileImage(attributes.getPicture()).nickname(attributes.getName()).isLock(false).build();
        member.updateMember(attributes);
        repository.save(member);
        attributes.setMember(member);
    }
}