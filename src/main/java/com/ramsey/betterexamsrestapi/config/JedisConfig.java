package com.ramsey.betterexamsrestapi.config;

import com.ramsey.betterexamsrestapi.service.util.PropertiesReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
@RequiredArgsConstructor
public class JedisConfig {
	
	private final PropertiesReaderService propertiesReader;
	
	@Bean
	public Jedis jedis() {
		
		return new Jedis(
				propertiesReader.get("host", PropertiesReaderService.REDIS),
				(Integer) propertiesReader.getObject("port", PropertiesReaderService.REDIS)
		);
		
	}
	
}
