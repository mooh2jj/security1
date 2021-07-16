package com.cos.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauthUserService extends DefaultOAuth2UserService {

	// 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration: "+userRequest.getClientRegistration());	// registrationId로 어떤 OAuth로 로그인
		System.out.println("getAccessToken: "+userRequest.getAccessToken().getTokenValue());
		// 구글로그인 버큰 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client라이브러리) -> AccessToken 요청
		// userRequest 정보 -> 회원프로필 받아야함(loadUser함수 호출) -> 구글로부터 회원프로필 받아준다
		System.out.println("getAdditionalParameters: "+userRequest.getAdditionalParameters());
		System.out.println("super.loadUser(userRequest).getAttributes: "+super.loadUser(userRequest).getAttributes());

		OAuth2User oAuth2User = super.loadUser(userRequest);

		return super.loadUser(userRequest);
	}

	
}
