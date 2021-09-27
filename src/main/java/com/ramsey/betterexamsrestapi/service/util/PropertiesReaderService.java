package com.ramsey.betterexamsrestapi.service.util;

import com.ramsey.betterexamsrestapi.error.NotSupportedError;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Service
public class PropertiesReaderService {
	
	public static final int REDIS = 1;
	public static final int EMAIL = 2;
	private static final String JEDIS_FILE = "jedis.properties";
	private static final String EMAIL_FILE = "email.properties";
	private Properties jedisProperties;
	private Properties emailProperties;
	
	@PostConstruct
	@SneakyThrows
	public void init() {
		
		jedisProperties = new Properties();
		jedisProperties.load(getClass().getClassLoader().getResourceAsStream(JEDIS_FILE));
		emailProperties = new Properties();
		emailProperties.load(getClass().getClassLoader().getResourceAsStream(EMAIL_FILE));
		
	}
	
	public String get(String key, int type) {
		
		Properties properties = getProperties(type);
		
		if(properties == null) {
			
			throw new NotSupportedError();
			
		}
		
		return properties.getProperty(key);
		
	}
	
	public Object getObject(String key, int type) {
		
		Properties properties = getProperties(type);
		
		if(properties == null) {
			
			throw new NotSupportedError();
			
		}
		
		return properties.get(key);
		
	}
	
	private Properties getProperties(int type) {
		
		switch(type) {
			
			case REDIS:
				return jedisProperties;
			case EMAIL:
				return emailProperties;
			
		}
		
		return null;
		
	}
	
}
