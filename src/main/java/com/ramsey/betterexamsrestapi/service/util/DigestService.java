package com.ramsey.betterexamsrestapi.service.util;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class DigestService {
	
	private static final String API_SECRET_CODE = "a1b2c33d4e5f6g7h8i9jakblc";
	private static final String ALGORITHM = "HmacMD5";
	
	@SneakyThrows
	public String generateHmac(String message) {
		
		Mac md5MAC;
		md5MAC = Mac.getInstance(ALGORITHM);
		SecretKeySpec keySpec = new SecretKeySpec(API_SECRET_CODE.getBytes(StandardCharsets.UTF_8), ALGORITHM);
		md5MAC.init(keySpec);
		return Base64.getEncoder().encodeToString(md5MAC.doFinal(message.getBytes(StandardCharsets.UTF_8)));
		
	}
	
	public String digestRequestGenerator(
			String username,
			String realm,
			String password,
			String nonce,
			String date,
			String method,
			String body,
			String resource
	) {
		
		// HA1 = MD5(username:realm:password)
		// response = MD5(HA1:nonce:date:method:body:resource:response)
		final String HA1 = generateHmac(
				String.format(
						"%s:%s:%s",
						username,
						realm,
						password
				)
		);
		return generateHmac(
				String.format(
						"%s:%s:%s:%s:%s:%s",
						HA1,
						nonce,
						date,
						method,
						body,
						resource
				)
		);
		
	}
	
	public String digestResponseGenerator(
			String username,
			String realm,
			String password,
			String nonce,
			String date,
			String method,
			String body,
			String resource,
			String response
	) {
		
		// HA1 = MD5(username:realm:password)
		// response = MD5(HA1:nonce:date:method:body:resource:response)
		final String HA1 = generateHmac(
				String.format(
						"%s:%s:%s",
						username,
						realm,
						password
				)
		);
		return generateHmac(
				String.format(
						"%s:%s:%s:%s:%s:%s:%s",
						HA1,
						nonce,
						date,
						method,
						body,
						resource,
						response
				)
		);
		
	}
	
}
