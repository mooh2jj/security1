package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

// CRUD 함수를 JpaRepository가 들고 있음
// @Repository라는 어노테이션이 없어도 IoC된다. 이유는 JpaRepository를 상속했기 때문에
public interface UserRepository extends JpaRepository<User, Integer> {

	// findBy규칙 -> 그다음은 문법문제
	// select * from user where username = ? 
	// 이런 걸 Query Method라고 불림
	public User findByUsername(String username);	

}
