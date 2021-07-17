package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauthUserService extends DefaultOAuth2UserService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	// 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration: "+userRequest.getClientRegistration());	// registrationId로 어떤 OAuth로 로그인
		System.out.println("getAccessToken: "+userRequest.getAccessToken().getTokenValue());

		OAuth2User oAuth2User = super.loadUser(userRequest);
		// 구글로그인 버큰 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client라이브러리) -> AccessToken 요청
		// userRequest 정보 -> 회원프로필 받아야함(loadUser함수 호출) -> 구글로부터 회원프로필 받아준다
		System.out.println("getAdditionalParameters: "+userRequest.getAdditionalParameters());
		System.out.println("getAttributes: "+oAuth2User.getAttributes());

		// oauth 회원가입 강제 등록
		String provider = userRequest.getClientRegistration().getClientId();	// google
		String providerId = oAuth2User.getAttribute("sub");
		String username = provider + "_" + providerId;							// google_1097~~
		String password = bCryptPasswordEncoder.encode("getitthere");
		String email = oAuth2User.getAttribute("email");
		String role = "ROLE_USER";

		User userEntity = userRepository.findByUsername(username);

		if (userEntity == null) {
			System.out.println("구글 로그인 최초");
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		} else {
			System.out.println("구글 로그인을 이미 했음, 자동회원가입이 되어있다.");
		}

		return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
	}

	
}
