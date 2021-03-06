package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

		String result = "";
		System.out.println("/test/oauth/login ============");
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("authentication: " + oAuth2User.getAttributes());
		System.out.println("OAuth2User: "+ oAuth.getAttributes());

		ObjectMapper objectMapper = new ObjectMapper();

		try {
			result = objectMapper.writeValueAsString(oAuth);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return result;
	}


	// http://localhost:8082/
	@GetMapping("/")	
	public String index() {
		// ???????????? ??? ???????????? ?????? -> html??? ????????? ????????? ????????? ??????. WebMvcConfig??? mustache -> html ??????!
		System.out.println("#########");
		return "index";	// => src/main/resources/templates/index.mustache
	}

	@GetMapping("/user")	
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails: "+ principalDetails.getUser());
		return "user";	
	}

	
	@GetMapping("/loginForm")	// ???????????? ???????????? ?????? ??????!
	public String loginForm() {
		
		return "loginForm";	
	}

	@GetMapping("/logout.do")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("logout");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}

		return "redirect:/loginForm";
	}
	
	@GetMapping("/joinForm")	// ???????????? ???????????? ??????
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
		userRepository.save(user);	// ???????????? ?????? but, ??????????????? ???????????? ??? ??? ??????! ???????????? ???????????? ???????????? ????????? => bCryptPasswordEncoder ?????????!
		return "redirect:/loginForm";	
	}

	// ????????? ??????????????? ??????????????? ?????? ??????!
	// 1) securedEnabled
	@Secured("ROLE_ADMIN")	// @EnableGlobalMethodSecurity(securedEnabled = true) : secured ??????????????? ?????????
	@GetMapping("/admin")	
	public @ResponseBody String admin() {
		return "admin";	
	}

	// 2) prePostEnabled
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")    // @EnableGlobalMethodSecurity(prePostEnabled = true) : secured ??????????????? ?????????
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
}
