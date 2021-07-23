package com.cos.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.security1.config.oauth.PrincipalOauthUserService;

@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)	// secured 어노테이션 활성화, preAuthorized 어노테이션 활성화
public class SecurtiyConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalOauthUserService principalOauthUserService;
	
	// @Bean: 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
				.antMatchers("/user/**").authenticated()								// 인증(로그인)만 되면 들어갈 수 있는 주소!
				.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
				.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")	// admin role이 있어야 '/admin'페이지에 들어 갈수있다.
				.anyRequest().permitAll()	//  나머진 url 요청에는 다 허용
			.and()
				.formLogin()	// 로그인 관련 설정
				.loginPage("/loginForm")
				.loginProcessingUrl("/login")	// login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다. -> controller에 /login을 안만들어도 된다!
				.defaultSuccessUrl("/")			// login이 성공하면 "/" : index 페이지로 간다.
			.and()
				.logout()
				.logoutUrl("/logout.do")
				.logoutSuccessUrl("/loginForm")
				.invalidateHttpSession(true)	// 로그아웃 이후 세션 전체 삭제 여부
				.clearAuthentication(true)
			.and()
				.oauth2Login()
				.loginPage("/loginForm")
					// 1. 코드받기(인증됨거), 2. 엑세스토큰(권한), 3.사용자프로필 정보를 가져옴. 4.그 정보를 토대로 회원가입을 자동으로 진행시킴.
					// 4-2. (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> 집주소, 백화점몰 -> vip등급, 일반등급
				.userInfoEndpoint()							// 구글 로그인이 완료된 뒤의 후처리가 필요함!
				.userService(principalOauthUserService);	// Tip. 코드 x (상태 엑세스토큰 + 사용자 프로필정보 바로 받아볼 수 있음.)
	}
	
}
