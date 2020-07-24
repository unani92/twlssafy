package com.web.curation.social.security;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;


import com.web.curation.social.service.CustomOAuth2UserService;

import static com.web.curation.social.security.SocialType.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/css/**, /static/**, *.ico"); // Resources 파일이나 Javascript 파일 경로 무시
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
        .csrf().disable()
        .authorizeRequests()            
                .antMatchers("/**")
                .permitAll()
                .antMatchers("/facebook").hasAuthority(FACEBOOK.getRoleType()).antMatchers("/google")
                .hasAuthority(GOOGLE.getRoleType()).antMatchers("/kakao").hasAuthority(KAKAO.getRoleType())
                .antMatchers("/naver").hasAuthority(NAVER.getRoleType()).anyRequest().authenticated().and()
                .oauth2Login().userInfoEndpoint().userService(new CustomOAuth2UserService())
                // 네이버 USER INFO의 응답을 처리하기 위한 설정
                .and().defaultSuccessUrl("/").failureUrl("/loginFailure").and().exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/user"))
                ;

    }

    private ClientRegistration getRegistration(OAuth2ClientProperties clientProperties, String client) {
        if ("google".equals(client)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("google");
            return CommonOAuth2Provider.GOOGLE.getBuilder(client).clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret()).scope("email", "profile").build();
        }
        if ("facebook".equals(client)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("facebook");
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client).clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,link").scope("email").build();
        }
        return null;
    }
}
