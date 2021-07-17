package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

import java.util.Optional;

@Controller
public class IndexController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/test/login")
	@ResponseBody
	public String loginTest(
			Authentication authentication,
			@AuthenticationPrincipal PrincipalDetails principalDetails2
			) {
		System.out.println("/test/login ============");
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication: " + principalDetails.getUser());

		System.out.println("userDetails: "+ principalDetails2.getUser());
		return "session check";
	}

	@GetMapping("/test/oauth/login")
	@ResponseBody
	public String loginOAuthTest(
			Authentication authentication,
			@AuthenticationPrincipal OAuth2User oAuth
			) {
		System.out.println("/test/oauth/login ============");
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("authentication: " + oAuth2User.getAttributes());
		System.out.println("OAuth2User: "+ oAuth.getAttributes());
		return "OAuth check";
	}


	// http://localhost:8082/
	@GetMapping("/")	
	public @ResponseBody String index() {
		// 머스테치 로 기본으로 설정 -> html로 할려면 설정을 더해야 한다. WebMvcConfig로 mustache -> html 설정!
		return "index";	// => src/main/resources/templates/index.mustache
	}

	@GetMapping("/user")	
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails: "+ principalDetails.getUser());
		return "user";	
	}
	
	@GetMapping("/manager")	
	public @ResponseBody String manager() {
		
		return "manager";	
	}
	
	@GetMapping("/loginForm")	// 시큐리티 낚아채지 않게 설정!
	public String loginForm() {
		
		return "loginForm";	
	}
	
	@GetMapping("/joinForm")	// 회원가입 페이지로 이동
	public String joinForm() {
//		System.out.println("joinForm controller start...");
		return "joinForm";	
	}
	
	@PostMapping("/join")	
	public String join(User user) {
		System.out.println("join start... , user: "+user);
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		userRepository.save(user);	// 회원가입 잘됨 but, 시큐리티로 로그인을 할 수 없음! 패스워드 암호화가 안되었기 때문에 => bCryptPasswordEncoder 해야됨!
		return "redirect:/loginForm";	
	}

	// 특정한 메서드에만 시큐리티를 걸고 싶다!
	// 1) securedEnabled
	@Secured("ROLE_ADMIN")	// @EnableGlobalMethodSecurity(securedEnabled = true) : secured 어노테이션 활성화
	@GetMapping("/admin")	
	public @ResponseBody String admin() {
		return "admin";	
	}

	// 2) prePostEnabled
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")    // @EnableGlobalMethodSecurity(prePostEnabled = true) : secured 어노테이션 활성화
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "data";
	}
	
}
