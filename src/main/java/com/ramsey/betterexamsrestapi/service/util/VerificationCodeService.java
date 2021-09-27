package com.ramsey.betterexamsrestapi.service.util;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class VerificationCodeService {
	
	@SneakyThrows
	public String generateCode(Integer length) {
		
		char[] chars = {
				'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
				'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
				'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
		};
		SecureRandom random = SecureRandom.getInstanceStrong();
		StringBuilder stringBuffer = new StringBuilder();
		
		for(int i = 0; i < length; ++i) {
			
			stringBuffer.append(chars[random.nextInt(chars.length)]);
			
		}
		
		return stringBuffer.toString();
		
	}
	
}
