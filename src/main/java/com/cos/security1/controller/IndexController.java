package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	// http://localhost:8082/
	@GetMapping("/")	
	public String index() {
		
		return "index";	// src/main/resources/templates/index.mustache
	}
}
