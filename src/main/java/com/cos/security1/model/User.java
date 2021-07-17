package com.cos.security1.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@NoArgsConstructor
@Data
public class User {

	@Id	// pk
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String password;
	private String email;
	private String role;	// ROLE_USER, ROLE_ADMIN
//	private Timestamp loginDate;	// 아직 할 때는 아님..

	private String provider;	// ex. google
	private String providerId;	// sub=116867573910474435505

	@CreationTimestamp
	private Timestamp createDate;

	@Builder
	public User(String username, String password, String email, String role, String provider, String providerId, Timestamp createDate) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
		this.createDate = createDate;
	}

}
