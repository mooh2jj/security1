package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.security1.model.User;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인을 진행이 완료가 되면 시큐리티 session을 만들어준다. (SecurityContextHolder)
// 오브젝트 타입 => Authentication 타입 객체
// Authentication 안에 User정보가 있어야 함.
// User오브젝트타입 => UserDetails 타입 객체

// Security Session => Authentication 객체 만들기(PrincipalDetailsService) => UserDetails(PrincipalDetails)

public class PrincipalDetails implements UserDetails{

	private User user;		// 콤포지션
	
	public PrincipalDetails(User user) {		// PrincipalDetails 안에 User 정보를 넣기 위해 생성자에 셋팅!
		this.user = user;
	}
	
	// 해당 User의 권한을 리턴하는 곳!
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}	// 기간만료 안됐니? -> true

	@Override
	public boolean isEnabled() {
		
		// 우리 사이트에 1년동안 회원이 로그인을 안하는 휴먼계정으로 한다면,
		// 현재시간 - 로그인시간 => 1년 초과하면 return false;
		return true;
	}

}
