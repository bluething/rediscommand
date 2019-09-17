package com.pegipegi.rediscommand;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.embedded.RedisServer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ZINCRBYIT {
	
	private static RedisServer redisServer = new RedisServer(6379);
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	private String redisKey = "thekey";
	
	@BeforeClass
	static public void setup() {
		redisServer.start();
	}
	
	@AfterClass
	static public void tearDown() {
		redisServer.stop();
	}
	
	@After
	public void cleanUp() {
		stringRedisTemplate.delete(redisKey);
	}
	
	/*
	 * https://redis.io/commands/zincrby
	 * see https://redis.io/commands/zrevrangebyscore
	 * */
	@Test
	public void zincrbyGivenNewMemberThenScoreIsOne() {
		//given
		stringRedisTemplate.opsForZSet().incrementScore(redisKey, "numberOne", 1.0d);
		
		//when
		Set<ZSetOperations.TypedTuple<String>> scores = stringRedisTemplate
				.opsForZSet().reverseRangeWithScores(redisKey, 0, Long.MAX_VALUE);
		
		//then
		Map<String, Double> keyValue = new HashMap<>();
		scores.forEach(s -> keyValue.put(s.getValue(), s.getScore()));
		assertEquals(Double.valueOf(1.0), keyValue.get("numberOne"));
		
	}
	
	/*
	 * https://redis.io/commands/zincrby
	 * see https://redis.io/commands/zrevrangebyscore
	 * */
	@Test
	public void zincrbyGivenTenThousandMemberWithRandomScoreThenGetTenHighestMember() {
		//given
		for(int i = 1; i<=9990; i++) {
			stringRedisTemplate.opsForZSet()
			.incrementScore(redisKey, "number"+i, ThreadLocalRandom.current().nextDouble(1, 10));
		}
		for(int i = 9991; i<=10000; i++) {
			stringRedisTemplate.opsForZSet()
			.incrementScore(redisKey, "number"+i, 10);
		}
		
		//when
		Set<ZSetOperations.TypedTuple<String>> member = stringRedisTemplate
				.opsForZSet().reverseRangeByScoreWithScores(redisKey, 0, Long.MAX_VALUE, 0, 10);
		
		//then
		Map<String, Double> keyValue = new HashMap<>();
		member.forEach(s -> keyValue.put(s.getValue(), s.getScore()));
		assertEquals(Double.valueOf(10), keyValue.get("number10000"));
	}

}
