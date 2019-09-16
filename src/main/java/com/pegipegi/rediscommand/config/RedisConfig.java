package com.pegipegi.rediscommand.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory(
				new RedisStandaloneConfiguration("127.0.0.1", 6379));
		connectionFactory.getPoolConfig().setMaxTotal(5);
		
		return connectionFactory;
	}
	
	@Bean
	public StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisConnectionFactory());
		stringRedisTemplate.setEnableTransactionSupport(true);
		return stringRedisTemplate;
	}
	
	

}
