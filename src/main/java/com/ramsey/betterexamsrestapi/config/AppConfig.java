package com.ramsey.betterexamsrestapi.config;

import com.ramsey.betterexamsrestapi.service.util.DigestService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
	
	private final DigestService digestService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new PasswordEncoder() {
			
			@Override
			public String encode(CharSequence password) {
				
				return digestService.generateHmac(password.toString());
				
			}
			
			@Override
			public boolean matches(CharSequence password, String actualPassword) {
				
				return password.equals(actualPassword);
				
			}
			
		};
		
	}
	
}
