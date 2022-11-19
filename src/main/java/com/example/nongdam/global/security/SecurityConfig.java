package com.example.nongdam.global.security;

import com.example.nongdam.api.ExceptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtFilter;
    private final OAuthLoginProvider oauthProvider;
    private final OAuth2AuthSuccessHanler successHandler;

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers("/css/**", "/fonts/**", "/img/**", "/js/**", "/close", "/webjars/**", "/manage/**");
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        //httpSecurity.httpBasic().disable();
        httpSecurity
                .cors()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/assert/**",
                        "/actuator/**",
                        "/profile",
                        "/member/**",
                        "/member",
                        "/news",
                        "/crops",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/instances/**",
                        "/h2-console/**",
                        "/static/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oauthProvider)
                .and()
                .successHandler(successHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        PrintWriter writer = response.getWriter();
                        ObjectWriter mapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
                        response.setHeader("Content-Type","application/json; charset=UTF-8;");
                        writer.println(mapper.writeValueAsString(new ExceptionDto("로그인이 필요한 서비스 입니다.","request")));
                    }
                });
        return httpSecurity.build();
    }
    @Bean
    public BCryptPasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

}
