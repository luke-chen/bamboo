package com.luke.cms.service;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisTestService {
	private static final Logger logger = LoggerFactory.getLogger(RedisTestService.class);
	
	@Autowired
	@Qualifier("myJedisPool")
	private JedisPool jedisPool;
	
	@PostConstruct
	public void testJedis() {
		// test redis connection
		try {
			Jedis test = jedisPool.getResource();
			test.set("foo", "test foo");
			if(test.get("foo").equals("test foo") && test.del("foo") == 1) {
				logger.info("test redis: pass!");
				jedisPool.returnResource(test);
			}
			else {
				logger.info("test redis: failed!");
			}
		} catch(Exception e) {
			logger.error("test redis: failed", e);
		}
	}
	
	@PreDestroy
	public void freeJedisPoll() {
		if(jedisPool != null)
			jedisPool.destroy();
	}
	
	public void set(String key, String value) {
		Jedis redis = jedisPool.getResource();
		try {
			redis.set(key, value);
		} catch(Exception e) {
			logger.error("set redis error", e);
		} finally {
			jedisPool.returnResource(redis);
		}
	}
	
	public static void main(String[] str) {
		JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "ezine-redis-m1.nhorizon.in", 6387);
		// You can store the pool somewhere statically, it is thread-safe.
		// JedisPoolConfig includes a number of helpful Redis-specific
		// connection pooling defaults.
		// For example, Jedis with JedisPoolConfig will close a connection after
		// 300 seconds if it has not been returned.
		// You use it by:

		// Jedis implements Closable. Hence, the jedis instance will be
		// auto-closed after the last statement.
		try {
			// / ... do stuff here ... for example
			// jedis.set("foo", "bartttt");
			// frontJedis.del("foo");
			// String foobar = frontJedis.get("foo");
			// System.out.println(foobar);
			// frontJedis.publish("ezine-front-outsideAdv", "disable-mogo");
			
			System.out.println(jedisPool.getNumActive());
			Jedis test = jedisPool.getResource();
			test = jedisPool.getResource();
			System.out.println(jedisPool.getNumActive());
			test.set("foo", "test foo");
			System.out.println(test.get("foo"));
			test.del("foo");
			System.out.println(test.get("foo"));
			jedisPool.returnResource(test);
			System.out.println(jedisPool.getNumActive());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ... when closing your application:
		jedisPool.destroy();
	}
}
